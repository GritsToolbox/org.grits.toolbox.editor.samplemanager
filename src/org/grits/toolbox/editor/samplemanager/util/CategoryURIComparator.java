/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.util;

import java.util.Comparator;

import org.grits.toolbox.entry.sample.config.CategoryLayoutOrder;

/**
 * 
 *
 */
public class CategoryURIComparator implements Comparator<String>
{
    @Override
    public int compare(String categoryURI1, String categoryURI2)
    {
        return CategoryLayoutOrder.getCategoryRank(categoryURI1) > 
                CategoryLayoutOrder.getCategoryRank(categoryURI2) ? 1 : -1;
    }

}
