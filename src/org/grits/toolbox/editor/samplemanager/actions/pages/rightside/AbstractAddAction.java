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
public abstract class AbstractAddAction extends Action
{
    protected AbstractManagementPage page = null;
    protected TableViewer tableViewer = null;
    protected GritsOntologyManagerApi ontologyManagerApi = null;
    protected OntModel standardOntology;
    protected OntModel localOntology;
    protected ManagementEditor editor;

    public AbstractAddAction(AbstractManagementPage page, TableViewer tableViewer) {
        this.page  = page;
        this.tableViewer  = tableViewer;
        initDataSource();
        this.setupUI();
    }

    protected void initDataSource()
    {
        editor = ((ManagementEditor) this.page.getEditor());
        this.ontologyManagerApi  = editor.getOntologyManagerApi();
        standardOntology = editor.getStandardOntologyModel();
        localOntology = editor.getLocalOntologyModel();
    }

    protected void setupUI() {
        this.setToolTipText("Add to the list");
        ImageDescriptor imageDesc = ImageShare.ADD_ICON;
        Image image = imageDesc.createImage();
        this.setImageDescriptor(ImageDescriptor.createFromImage(image));
    }

    public abstract void run();
    
    public void refreshModifed()
    {
        this.tableViewer.refresh();
        ((ManagementEditor) this.page.getEditor()).markDirty();
    }

}
