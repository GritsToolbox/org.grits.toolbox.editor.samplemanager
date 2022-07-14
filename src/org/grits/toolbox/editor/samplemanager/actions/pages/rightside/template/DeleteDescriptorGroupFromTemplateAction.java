/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.template;

import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractDeleteAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class DeleteDescriptorGroupFromTemplateAction extends
        AbstractDeleteAction
{

    public DeleteDescriptorGroupFromTemplateAction(AbstractManagementPage page,
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
            DescriptorGroup selectedDescriptorGroup = (DescriptorGroup) tableViewer.getElementAt(selectionIndex);

            boolean removed = false;
            for(DescriptorGroup dg : categoryTemplate.getMandatoryDescriptorGroups())
            {
                if(dg.getUri().equals(selectedDescriptorGroup.getUri()))
                {
                    removed = categoryTemplate.getMandatoryDescriptorGroups().remove(selectedDescriptorGroup);
                    break;
                }
            }
            if(!removed)
            {
                for(DescriptorGroup dg : categoryTemplate.getOptionalDescriptorGroups())
                {
                    if(dg.getUri().equals(selectedDescriptorGroup.getUri()))
                    {
                        removed = categoryTemplate.getOptionalDescriptorGroups().remove(selectedDescriptorGroup);
                        break;
                    }
                }
            }
            this.ontologyManagerApi.removeDescriptorOrGroupFromTemplate(categoryTemplate.getTemplateURI(), categoryTemplate.getUri(), 
                    selectedDescriptorGroup.getUri());
            this.refreshModifed();
        }
    }

}
