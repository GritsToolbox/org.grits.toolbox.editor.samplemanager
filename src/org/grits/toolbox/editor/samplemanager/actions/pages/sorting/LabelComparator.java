/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.sorting;

import java.util.Comparator;

import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;

/**
 * 
 *
 */
public class LabelComparator implements Comparator<ClassesWithFeatures>
{
    public Boolean ascending = true;
    
    public void toggle()
    {
        ascending = !ascending;
    }

    @Override
    public int compare(ClassesWithFeatures c1, ClassesWithFeatures c2)
    {
            return ascending ? 
                    c1.getLabel().compareToIgnoreCase(c2.getLabel()) 
                    : (-1*c1.getLabel().compareToIgnoreCase(c2.getLabel()));
    }

}
