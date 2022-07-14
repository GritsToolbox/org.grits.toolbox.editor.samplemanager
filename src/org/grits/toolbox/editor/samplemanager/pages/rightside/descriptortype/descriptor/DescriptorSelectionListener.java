/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor;

import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;

/**
 * 
 *
 */
public class DescriptorSelectionListener extends RightSideSelectionListener
{

    public DescriptorSelectionListener(AbstractManagementPage page)
    {
        rightSideSection  = new RightSideDescriptorSection(page);
    }

    @Override
    protected void setSelection(ClassesWithFeatures selectedObject)
    {
        ((RightSideDescriptorSection) rightSideSection).setSelectedObject(selectedObject);
    }
}
