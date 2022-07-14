/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.dialogs.addunit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.grits.toolbox.entry.sample.config.ImageRegistry;
import org.grits.toolbox.entry.sample.config.ImageRegistry.SampleImage;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * 
 *
 */
public class AddMeasurementUnitDialog extends Dialog
{

    private OntModel unitOntologyModel;
    private Text searchFieldText;
    private TreeViewer treeViewer;
    private Text descriptionText;
    private List<OntClass> searchResults = new ArrayList<OntClass>();
    private MeasurementUnit selectedMeasurementUnit = null;
    private List<String> existingURIs;

    public AddMeasurementUnitDialog(Shell parentShell, OntModel unitOntologyModel)
    {
        super(parentShell);
        this.unitOntologyModel = unitOntologyModel;
    }

    @Override
    public void create()
    {
        super.create();
        getButton(OK).setEnabled(false);
    }
    
    public MeasurementUnit getSelectedMeasurementUnit()
    {
        return selectedMeasurementUnit;
    }
    
    protected Control createDialogArea(Composite parent)
    {
        Composite comp = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(comp, SWT.NONE);
        container.getShell().setText("Measurement Unit");
        container.getShell().setImage(ImageRegistry.getImageDescriptor(
        		SampleImage.EDIT_NAME_ICON).createImage());
        GridData containerData = new GridData(SWT.FILL, SWT.FILL, false, false);
        containerData.widthHint = 500;
        container.setLayoutData(containerData);
        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);

        Label label = new Label(container, SWT.NONE);
        label.setText("Search an Item");
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        searchFieldText = new Text(container, SWT.FILL|SWT.BORDER);
        GridData searchFieldTextGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        searchFieldTextGridData.grabExcessHorizontalSpace = true;
        searchFieldText.setLayoutData(searchFieldTextGridData);

        Label label2 = new Label(container, SWT.NONE);
        label2.setText("Matching Items");
        label2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        treeViewer = new TreeViewer(container, SWT.BORDER|SWT.H_SCROLL | SWT.V_SCROLL);
        GridData treeViewertGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        treeViewertGridData.grabExcessHorizontalSpace = true;
        treeViewertGridData.heightHint = 150;
        treeViewer.getTree().setLayoutData(treeViewertGridData);

        descriptionText = new Text(container, SWT.READ_ONLY | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        GridData descriptionTextGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        descriptionTextGridData.grabExcessHorizontalSpace = true;
        descriptionTextGridData.heightHint = 50;
        descriptionText.setLayoutData(descriptionTextGridData);

        treeViewer.setLabelProvider(new MeasurementUnitLabelProvider());
        treeViewer.setContentProvider(new MeasurementUnitContentProvider());
        ViewerFilter searchFilter = new ViewerFilter()
        {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element)
            {
                String searchText = searchFieldText.getText();
                boolean show = false;

                if(element instanceof OntClass)
                {
                    OntClass ontClass = (OntClass) element;
                    if(!existingURIs.contains(ontClass.getURI()))
                    {
                        if(searchText.equals("*"))
                        {
                            show = true;
                        }
                        else
                        {
                            if(ontClass.getLabel(null).contains(searchText))
                            {
                                show = true;
                                searchResults.add(ontClass);
                            }
                            else if(!ontClass.listSubClasses(true).toList().isEmpty())
                            {
                                for(OntClass subClass : ontClass.listSubClasses(true).toList())
                                {
                                    if(subClass.getLabel(null).contains(searchText))
                                    {
                                        show = true;
                                        searchResults.add(subClass);
//                                      break;
                                    }
                                }
                            }
                        }
                    }
                }
                return show;
            }
        };
        ViewerFilter[] filters = new ViewerFilter[]{searchFilter};
        treeViewer.setFilters(filters);
        List<OntClass> classes = unitOntologyModel.listClasses().toList();
        OntClass unitClass = null;
        for(OntClass ontClass : classes)
        {
            if((ontClass).getURI().equals("http://purl.obolibrary.org/obo/UO_0000000"))
            {
                unitClass = ontClass;
                break;
            }
        }
        treeViewer.setInput(unitClass);
        treeViewer.expandAll();
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                if(!treeViewer.getSelection().isEmpty())
                {
                    TreeItem firstSelectedItem = treeViewer.getTree().getSelection()[0];
                    if(firstSelectedItem.getData() instanceof OntClass)
                    {
                        OntClass ontClass = (OntClass) firstSelectedItem.getData();
                        OntProperty definition = unitOntologyModel.getOntProperty("http://purl.obolibrary.org/obo/IAO_0000115");
                        List<RDFNode> results = unitOntologyModel.listObjectsOfProperty(ontClass.asResource(), definition).toList();
                        String comment = "";
                        if(!results.isEmpty())
                        {
                            comment = results.iterator().next().toString();
                            comment = comment == null ? "" : comment;
                            int index = comment.lastIndexOf("^^http://www.w3.org/2001/XMLSchema");
                            comment = comment.substring(0, index);
                        }
                        descriptionText.setText(comment);
                        boolean enable = ontClass.listSubClasses(true).toList().isEmpty() ? true : false;
                        getButton(OK).setEnabled(enable);
                    }
                }
            }
        });

        searchFieldText.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent e)
            {
                getButton(OK).setEnabled(false);
                treeViewer.refresh();
                treeViewer.expandAll();
                descriptionText.setText("");
            }
        });
        return comp;
    }

    @Override
    protected void okPressed()
    {
        TreeItem firstSelectedItem = treeViewer.getTree().getSelection()[0];
        if(firstSelectedItem.getData() instanceof OntClass)
        {
            OntClass selectedUnit = (OntClass) firstSelectedItem.getData();
            selectedMeasurementUnit = new MeasurementUnit();
            selectedMeasurementUnit.setUri(selectedUnit.getURI());
            selectedMeasurementUnit.setLabel(selectedUnit.getLabel(null));
            super.okPressed();
        }
        else
        {
            super.cancelPressed();
        }
    }

    public void setExistingURIs(List<String> existingURIs)
    {
        this.existingURIs = existingURIs;
    }
}
