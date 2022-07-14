/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.input;

import java.util.List;

/**
 * 
 *
 */
public abstract class DescriptorTypeClassesWithFeatures extends ClassesWithFeatures
{

    public abstract Integer getMaxOccurrence();
    
    public abstract void setMaxOccurrence(Integer maxOccurrence);
    
    public abstract List<String> getCategories();

    public abstract void updateCategorySelection(String categoryUri, boolean selected);
}
