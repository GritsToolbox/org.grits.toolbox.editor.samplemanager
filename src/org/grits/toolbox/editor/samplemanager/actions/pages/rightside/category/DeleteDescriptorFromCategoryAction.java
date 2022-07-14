/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.category;

import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractDeleteAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.Category;
import org.grits.toolbox.entry.sample.model.Descriptor;

/**
 * 
 *
 */
public class DeleteDescriptorFromCategoryAction extends AbstractDeleteAction
{

    public DeleteDescriptorFromCategoryAction(AbstractManagementPage page,
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
            Descriptor selectedDescriptor = (Descriptor) tableViewer.getElementAt(selectionIndex);

            boolean removed = false;
            for(Descriptor desc : selectedCategory.getDescriptors())
            {
                if(desc.getUri().equals(selectedDescriptor.getUri()))
                {
                    removed = selectedCategory.getDescriptors().remove(selectedDescriptor);
                    break;
                }
            }
            if(removed)
            {
                this.ontologyManagerApi.updateCategorySelection(selectedCategory.getUri(), 
                        selectedDescriptor.getUri(), false);
                this.refreshModifed();
            }
        }
    }

}
