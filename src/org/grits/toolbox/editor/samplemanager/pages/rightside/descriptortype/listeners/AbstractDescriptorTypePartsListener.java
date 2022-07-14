/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.listeners;

import org.grits.toolbox.editor.samplemanager.input.DescriptorTypeClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.listeners.AbstractModificationListener;

/**
 * 
 *
 */
public abstract class AbstractDescriptorTypePartsListener extends
        AbstractModificationListener
{
    protected DescriptorTypeClassesWithFeatures selectedObject = null;

    public AbstractDescriptorTypePartsListener(AbstractManagementPage page)
    {
        super(page);
    }

    public void setSelectedObject(DescriptorTypeClassesWithFeatures selectedObject2)
    {
        this.selectedObject = selectedObject2;
    }
}
