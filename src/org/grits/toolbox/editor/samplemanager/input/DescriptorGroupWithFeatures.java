/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.input;

import java.util.List;

import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class DescriptorGroupWithFeatures extends DescriptorTypeClassesWithFeatures
{

    private DescriptorGroup descriptorGroup = null;

    public DescriptorGroupWithFeatures(DescriptorGroup descriptorGroup)
    {
        this.descriptorGroup = descriptorGroup;
    }

    /**
     * @return the descriptorGroup
     */
    public DescriptorGroup getDescriptorGroup()
    {
        return descriptorGroup;
    }

    /**
     * @param descriptorGroup the descriptorGroup to set
     */
    public void setDescriptorGroup(DescriptorGroup descriptorGroup)
    {
        this.descriptorGroup = descriptorGroup;
    }

    @Override
    public String getLabel()
    {
        return this.descriptorGroup.getLabel();
    }

    @Override
    public void setLabel(String label)
    {
        this.descriptorGroup.setLabel(label);
    }

    @Override
    public String getDescription()
    {
        return this.descriptorGroup.getDescription();
    }

    @Override
    public void setDescription(String description)
    {
        this.descriptorGroup.setDescription(description);

    }

    @Override
    public String getUri()
    {
        return this.descriptorGroup.getUri();
    }

    public Integer getMaxOccurrence() {
        return this.descriptorGroup.getMaxOccurrence();
    }

    @Override
    public void setMaxOccurrence(Integer maxOccurrence)
    {
        this.descriptorGroup.setMaxOccurrence(maxOccurrence);
    }

    @Override
    public List<String> getCategories()
    {
        return this.descriptorGroup.getCategories();
    }

    @Override
    public void updateCategorySelection(String categoryUri, boolean selected)
    {
        if(selected && !this.descriptorGroup.getCategories().contains(categoryUri))
        {
            this.descriptorGroup.addCategory(categoryUri);
        }
        else if(this.descriptorGroup.getCategories().contains(categoryUri))
        {
            this.descriptorGroup.getCategories().remove(this.descriptorGroup.getCategories().indexOf(categoryUri));
        }
    }
}
