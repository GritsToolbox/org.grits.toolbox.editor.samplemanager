/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.listeners;

import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.ontology.GritsOntologyManagerApi;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public abstract class AbstractModificationListener
{
    protected ClassesWithFeatures selectedObject = null;
    protected AbstractManagementPage page = null;
    protected GritsOntologyManagerApi ontoManagerApi = null;
    protected String initialLabel = null;

    public AbstractModificationListener(AbstractManagementPage page)
    {
        this.page  = page;
        setDataSource();
    }

    protected void setDataSource()
    {
        this.ontoManagerApi = ((ManagementEditor) this.page.getEditor())
                .getOntologyManagerApi();
    }

    public void setSelectedObject(ClassesWithFeatures selectedObject2)
    {
        this.selectedObject = selectedObject2;
        this.initialLabel = selectedObject2.getLabel();
    }

    public void update(Object modification)
    {
        this.updateDataSourcePart(modification);
        ((ManagementEditor) this.page.getEditor()).markDirty();
    }

    protected abstract void updateDataSourcePart(Object modification);

}
