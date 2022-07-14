/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class CheckMandatoryDescriptorInGroupListener extends AbstractAddAction implements ICheckStateListener
{
    public CheckMandatoryDescriptorInGroupListener(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
    }

    @Override
    public void checkStateChanged(CheckStateChangedEvent event)
    {
        boolean checked = event.getChecked();
        Descriptor descriptor = (Descriptor) event.getElement();
        CheckboxTableViewer tableViewer = (CheckboxTableViewer) event.getSource();
        DescriptorGroup descriptorGroup = (DescriptorGroup) tableViewer.getInput();
        if(checked)
        {
            descriptorGroup.getOptionalDescriptors().remove(descriptor);
            descriptorGroup.getMandatoryDescriptors().add(descriptor);
        }
        else
        {
            descriptorGroup.getMandatoryDescriptors().remove(descriptor);
            descriptorGroup.getOptionalDescriptors().add(descriptor);
        }
        this.updateOntology(descriptorGroup.getUri(),descriptor.getUri(), checked);
        this.refreshModifed();
    }

    private void updateOntology(String groupURI, String descriptorURI, boolean mandatory)
    {
        this.ontologyManagerApi.setMandatoryStateDescriptorInGroup(groupURI, descriptorURI, mandatory);
    }

    @Override
    public void run()
    {

    }

}
