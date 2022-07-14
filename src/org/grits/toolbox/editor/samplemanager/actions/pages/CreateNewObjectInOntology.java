/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages;

import java.util.List;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.LabelValidator;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public class CreateNewObjectInOntology extends AbstractAddAction
{

    private String classUri;
    //    private DialogWithLabel dialogWithLabel;
    private InputDialog dialogWithLabel;
    private LabelValidator labelValidator;

    public CreateNewObjectInOntology(AbstractManagementPage page,
            TableViewer tableViewer, String classUri)
    {
        super(page, tableViewer);
        this.classUri = classUri;
        setToolTipText("Add new to the Ontology");
        //        Shell shell = new Shell(page.getRightSectionOfPage().getShell());
        //      dialogWithLabel = new DialogWithLabel(shell );
        Shell shell = this.page.getRightSectionOfPage().getShell();
        labelValidator = new LabelValidator(getExistingLabels());
        dialogWithLabel = new InputDialog(shell, "New ", "Please give a unique label to it", "", labelValidator );
    }

    private List<String> getExistingLabels()
    {
        List<String> existingLabels = super.editor.getAllIndividualLabels(this.classUri);
        return existingLabels;
    }

    @Override
    public void run()
    {
        labelValidator.setExistingLabels(getExistingLabels());
        dialogWithLabel.open();
        if(dialogWithLabel.getReturnCode() == Window.OK)
        {
            String label = dialogWithLabel.getValue().trim();
            String objectURI = createNewIndividualWithLabel(label);
            this.page.addInput(label, objectURI);
            this.refreshModifed();
//            this.page.refreshPage();
//            int selectionIndex = addObjectToPage(label, objectURI);
//            leftSideTableViewer.refresh();
//            leftSideTableViewer.getTable().select(selectionIndex);
//            leftSideTableViewer.setSelection(leftSideTableViewer.getSelection());
        }
    }

    private String createNewIndividualWithLabel(String label)
    {
        return this.ontologyManagerApi.createNewIndividual(classUri, label);
    }

}
