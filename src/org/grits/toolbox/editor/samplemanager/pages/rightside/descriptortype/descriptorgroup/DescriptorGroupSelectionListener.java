/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup;

import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;

/**
 * 
 *
 */
public class DescriptorGroupSelectionListener extends RightSideSelectionListener
{

    public DescriptorGroupSelectionListener(AbstractManagementPage page)
    {
        rightSideSection  = new RightSideDescriptorGroupSection(page);
    }

    public void setSelection(ClassesWithFeatures selectedDescriptorGroup)
    {
        ((RightSideDescriptorGroupSection) this.rightSideSection).setSelectedObject(selectedDescriptorGroup);
    }

}
