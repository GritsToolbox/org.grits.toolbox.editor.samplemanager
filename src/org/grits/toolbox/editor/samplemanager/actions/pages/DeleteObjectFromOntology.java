/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractDeleteAction;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public class DeleteObjectFromOntology extends AbstractDeleteAction
{
    public DeleteObjectFromOntology(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
        setToolTipText("Delete from the ontology");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {
        selectionIndex =  tableViewer.getTable().getSelectionIndex();
        if(selectionIndex != -1)
        {
            ClassesWithFeatures classWithFeatures = (ClassesWithFeatures) tableViewer.getElementAt(selectionIndex);
            if(classWithFeatures.isEditable())
            {
            this.ontologyManagerApi.removeIndividual(classWithFeatures.getUri());
            ((List<ClassesWithFeatures>) tableViewer.getInput()).remove(classWithFeatures);
//            leftSideTableViewer.getTable().select(0);
            this.refreshModifed();
            }
            else
            {
                MessageDialog.openInformation(this.page.getLeftSectionOfPage().getShell(), 
                        "Cannot delete the selected item", 
                        "You do not have the permission to delete item from the standard ontology.");
            }
        }
    }

}
