/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.util;

import java.util.Comparator;

import org.grits.toolbox.entry.sample.config.CategoryLayoutOrder;
import org.grits.toolbox.entry.sample.model.Category;

/**
 * 
 *
 */
public class CategoryObjectComparator implements Comparator<Category>
{
    @Override
    public int compare(Category category1, Category category2)
    {
        return CategoryLayoutOrder.getCategoryRank(category1.getUri()) > 
                CategoryLayoutOrder.getCategoryRank(category2.getUri()) ? 1 : -1;
    }

}
