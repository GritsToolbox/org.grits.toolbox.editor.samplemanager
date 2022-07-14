/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.input;

import java.util.List;

import org.grits.toolbox.entry.sample.model.Descriptor;

/**
 * 
 *
 */
public class DescriptorWithFeatures extends DescriptorTypeClassesWithFeatures
{

    private Descriptor descriptor = null;

    public DescriptorWithFeatures(Descriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    /**
     * @return the descriptor
     */
    public Descriptor getDescriptor()
    {
        return descriptor;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(Descriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    @Override
    public String getLabel()
    {
        return this.descriptor.getLabel();
    }

    @Override
    public void setLabel(String label)
    {
        this.descriptor.setLabel(label);
    }
 
    @Override
    public String getDescription()
    {
        return this.descriptor.getDescription();
    }

    @Override
    public void setDescription(String description)
    {
        this.descriptor.setDescription(description);
        
    }

    @Override
    public String getUri()
    {
        return this.descriptor.getUri();
    }

    @Override
    public Integer getMaxOccurrence()
    {
        return this.descriptor.getMaxOccurrence();
    }

    @Override
    public void setMaxOccurrence(Integer maxOccurrence)
    {
        this.descriptor.setMaxOccurrence(maxOccurrence);
    }

    @Override
    public List<String> getCategories()
    {
        return this.descriptor.getCategories();
    }

    @Override
    public void updateCategorySelection(String categoryUri, boolean selected)
    {
        if(selected && !this.descriptor.getCategories().contains(categoryUri))
        {
            this.descriptor.addCategory(categoryUri);
        }
        else if(this.descriptor.getCategories().contains(categoryUri))
        {
            this.descriptor.getCategories().remove(this.descriptor.getCategories().indexOf(categoryUri));
        }
    }
}
