/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.category;

import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractDeleteAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.Category;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class DeleteDescriptorGroupFromCategoryAction extends
AbstractDeleteAction
{

    public DeleteDescriptorGroupFromCategoryAction(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
    }

    @Override
    public void run()
    {
        selectionIndex = tableViewer.getTable().getSelectionIndex();
        if(selectionIndex != -1)
        {
            Category selectedCategory = (Category) tableViewer.getInput();
            DescriptorGroup selectedDescriptorGroup = (DescriptorGroup) tableViewer.getElementAt(selectionIndex);

            boolean removed = false;
            for(DescriptorGroup dg : selectedCategory.getDescriptorGroups())
            {
                if(dg.getUri().equals(selectedDescriptorGroup.getUri()))
                {
                    removed = selectedCategory.getDescriptorGroups().remove(selectedDescriptorGroup);
                    break;
                }
            }
            if(removed)
            {
                this.ontologyManagerApi.updateCategorySelection(selectedCategory.getUri(), 
                        selectedDescriptorGroup.getUri(), false);
                this.refreshModifed();
            }
        }
    }

}
