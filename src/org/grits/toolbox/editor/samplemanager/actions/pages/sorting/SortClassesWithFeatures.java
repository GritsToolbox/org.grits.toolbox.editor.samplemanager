/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.sorting;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.entry.sample.utilities.AbstractAlphanumericSorter;

/**
 * 
 *
 */
public class SortClassesWithFeatures extends AbstractAlphanumericSorter
{
    private LabelComparator labelComparator;

    public SortClassesWithFeatures(TableViewer tableViewer)
    {
        super(tableViewer);
        labelComparator = new LabelComparator();
    }

    @SuppressWarnings("unchecked")
    public void run()
    {
        List<ClassesWithFeatures> classWithFeatures = (List<ClassesWithFeatures>) tableViewer.getInput();
        Collections.sort(classWithFeatures, labelComparator);
        labelComparator.toggle();
        super.setAscendingDescendingIcon(labelComparator.ascending);
        tableViewer.refresh();
    }

    public Boolean getAscending()
    {
        return labelComparator.ascending;
    }

    public void setAscending(boolean ascending)
    {
        labelComparator.ascending = ascending;
    }
}
