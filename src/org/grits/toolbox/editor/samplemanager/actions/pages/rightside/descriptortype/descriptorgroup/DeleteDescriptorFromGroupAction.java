/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptorgroup;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractDeleteAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class DeleteDescriptorFromGroupAction extends AbstractDeleteAction
{
    public DeleteDescriptorFromGroupAction(AbstractManagementPage page, CheckboxTableViewer tableViewer)
    {
        super(page, tableViewer);
    }

    public void run()
    {
        selectionIndex = tableViewer.getTable().getSelectionIndex();
        if(selectionIndex != -1)
        {
            DescriptorGroup descriptorGroup = (DescriptorGroup) tableViewer.getInput();
            Descriptor selectedDescriptor = (Descriptor) tableViewer.getElementAt(selectionIndex);
            boolean found = false;
            boolean removed = false;
            for(Descriptor desc : descriptorGroup.getMandatoryDescriptors())
            {
                if(desc.getUri().equals(selectedDescriptor.getUri()))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                for(Descriptor desc : descriptorGroup.getOptionalDescriptors())
                {
                    if(desc.getUri().equals(selectedDescriptor.getUri()))
                    {
                        found = true;
                        break;
                    }
                }
            }
            else
            {
                removed = descriptorGroup.getMandatoryDescriptors().remove(selectedDescriptor);
            }
            if(!removed)
            {
                removed = descriptorGroup.getOptionalDescriptors().remove(selectedDescriptor);
            }
            this.ontologyManagerApi.removeDescriptorFromGroup(descriptorGroup.getUri(), 
                    selectedDescriptor.getUri());
            this.refreshModifed();
        }
    }
}
