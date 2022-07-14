/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.core.img.ImageShare;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.ontology.GritsOntologyManagerApi;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * 
 *
 */
public abstract class AbstractDeleteAction extends Action
{
    protected AbstractManagementPage page = null;
    protected TableViewer tableViewer = null;
    protected GritsOntologyManagerApi ontologyManagerApi = null;
    protected OntModel standardOntology;
    protected OntModel localOntology;
    protected int selectionIndex;

    public AbstractDeleteAction(AbstractManagementPage page, TableViewer tableViewer) 
    {
        this.page  = page;
        this.tableViewer  = tableViewer;
        initDataSource();
        this.setupUI();
    }

    protected void initDataSource()
    {
        ManagementEditor editor = ((ManagementEditor) this.page.getEditor());
        this.ontologyManagerApi  = editor.getOntologyManagerApi();
        standardOntology = editor.getStandardOntologyModel();
        localOntology = editor.getLocalOntologyModel();
    }

    protected void setupUI() 
    {
        this.setToolTipText("Delete from the list");
        ImageDescriptor imageDesc = ImageShare.DELETE_ICON;
        Image image = imageDesc.createImage();
        this.setImageDescriptor(ImageDescriptor.createFromImage(image));

    }

    public abstract void run();
    

    protected void removeTripleFromOntology(String subjectURI, String propertyURI,
            String objectURI)
    {
        this.ontologyManagerApi.removeTriple(subjectURI, propertyURI, objectURI);
        this.refreshModifed();
    }

    public void refreshModifed()
    {
        this.tableViewer.refresh();
        if(selectionIndex != -1)
        {
            tableViewer.getTable().setSelection(
                    Math.min(tableViewer.getTable().getItemCount()-1, selectionIndex));
        }
        ((ManagementEditor) this.page.getEditor()).markDirty();
    }
}
