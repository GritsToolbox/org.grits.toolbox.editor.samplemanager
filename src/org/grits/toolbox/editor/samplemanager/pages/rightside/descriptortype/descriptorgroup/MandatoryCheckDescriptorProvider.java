/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;
import org.grits.toolbox.entry.sample.utilities.UtilityDescriptorDescriptorGroup;

/**
 * 
 *
 */
public class MandatoryCheckDescriptorProvider implements ICheckStateProvider
{
    private CheckboxTableViewer tableViewer;

    public MandatoryCheckDescriptorProvider(CheckboxTableViewer tableViewer)
    {
        this.tableViewer = tableViewer;
    }

    @Override
    public boolean isChecked(Object element)
    {
        List<String> mandatoryURIs = new ArrayList<String>();
        if(tableViewer.getInput() instanceof DescriptorGroup)
        {
            DescriptorGroup descriptorGroup = (DescriptorGroup) tableViewer.getInput();
            mandatoryURIs = UtilityDescriptorDescriptorGroup.getMandatoryURIs(descriptorGroup);
        }

        Descriptor desc = (Descriptor) element;
        return mandatoryURIs.contains(desc.getUri());
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ICheckStateProvider#isGrayed(java.lang.Object)
     */
    @Override
    public boolean isGrayed(Object element)
    {
        return false;
    }

    public void setGroup(DescriptorGroup descriptorGroup)
    {
        //        ArrayList<Descriptor> descriptors = new ArrayList<Descriptor>(descriptorGroup.getMandatoryDescriptors());
        //        descriptors.addAll(descriptorGroup.getOptionalDescriptors());
        //        mandUris = UtilityDescriptorDescriptorGroup.getMandatoryURIs(descriptorGroup );
    }
}
