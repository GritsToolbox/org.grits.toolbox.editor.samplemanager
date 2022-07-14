/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.template;

import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractDeleteAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.Descriptor;

/**
 * 
 *
 */
public class DeleteDescriptorFromTemplateAction extends AbstractDeleteAction
{

    public DeleteDescriptorFromTemplateAction(AbstractManagementPage page,
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
            CategoryTemplate categoryTemplate = (CategoryTemplate) tableViewer.getInput();
            Descriptor selectedDescriptor = (Descriptor) tableViewer.getElementAt(selectionIndex);

            boolean removed = false;
            for(Descriptor desc : categoryTemplate.getMandatoryDescriptors())
            {
                if(desc.getUri().equals(selectedDescriptor.getUri()))
                {
                    removed = categoryTemplate.getMandatoryDescriptors().remove(selectedDescriptor);
                    break;
                }
            }
            if(!removed)
            {
                for(Descriptor desc : categoryTemplate.getOptionalDescriptors())
                {
                    if(desc.getUri().equals(selectedDescriptor.getUri()))
                    {
                        removed = categoryTemplate.getOptionalDescriptors().remove(selectedDescriptor);
                        break;
                    }
                }
            }
            this.ontologyManagerApi.removeDescriptorOrGroupFromTemplate(categoryTemplate.getTemplateURI(), categoryTemplate.getUri(), 
                    selectedDescriptor.getUri());
            this.refreshModifed();
        }
    }

}
