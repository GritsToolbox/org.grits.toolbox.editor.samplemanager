/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.util;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * 
 *
 */
public class GenericViewerComparatorCheckboxTable extends ViewerComparator
{
    private int column;
    private boolean ascending;

    public int compare(Viewer viewer, Object obj1, Object obj2)
    {
        int rc = 0;
        if(viewer instanceof CheckboxTableViewer)
        {
            CheckboxTableViewer checkboxTableViewer = (CheckboxTableViewer) viewer;
            if(column == 0)
            {
                int value1 = checkboxTableViewer.getChecked(obj1) ? 1 : -1;
                int value2 = checkboxTableViewer.getChecked(obj2) ? 1 : -1;
                rc = value1 - value2;
            }
            else if(column > 0)
            {
                ITableLabelProvider labelProvider = (ITableLabelProvider) checkboxTableViewer.getLabelProvider();
                if(labelProvider.getColumnText(obj1, column) instanceof String 
                        && labelProvider.getColumnText(obj2, column) instanceof String)
                {
                    String text1 = labelProvider.getColumnText(obj1, column)== null ? "" : labelProvider.getColumnText(obj1, column);
                    String text2 = labelProvider.getColumnText(obj2, column)== null ? "" : labelProvider.getColumnText(obj2, column);
                    rc = this.getComparisionForIntOrChar(text1, text2);
                }
            }
            rc = ascending ? rc : -rc;
        }
        return rc;
    }
    
    private int getComparisionForIntOrChar(String text1, String text2)
    {
        try
        {
            int intValue1 = Integer.parseInt(text1);
            int intValue2 = Integer.parseInt(text2);
            return intValue1 - intValue2;
        } catch (NumberFormatException ex)
        {
            return text1.compareToIgnoreCase(text2);
        }
    }

    public void setColumn(int column)
    {
        this.column = column;
    }
    
    public void setAscending(boolean ascending)
    {
        this.ascending = ascending;
    }

    public boolean getAscending()
    {
        return this.ascending;
    }
}
