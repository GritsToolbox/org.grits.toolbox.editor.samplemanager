/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.util;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * 
 *
 */
public class GenericColumnsWithCheckboxSelectionListener implements SelectionListener
{
    private CheckboxTableViewer checkboxTableViewer = null;
    private GenericViewerComparatorCheckboxTable genericViewerComparatorCheckboxTable;

    public GenericColumnsWithCheckboxSelectionListener(
            CheckboxTableViewer checkboxTableViewer)
    {
        this.checkboxTableViewer = checkboxTableViewer;
        genericViewerComparatorCheckboxTable = 
                (GenericViewerComparatorCheckboxTable) checkboxTableViewer.getComparator();
    }

    @Override
    public void widgetSelected(SelectionEvent e)
    {
        TableColumn tableColumn = (TableColumn) e.getSource();
        Table table = tableColumn.getParent();
        boolean ascending = table.getSortDirection() == SWT.UP;
        int newDirection = ascending ? SWT.DOWN : SWT.UP;

        int column = table.indexOf(tableColumn);
        table.setSortDirection(newDirection);
        table.setSortColumn(table.getColumn(column));
        genericViewerComparatorCheckboxTable.setColumn(column);
        genericViewerComparatorCheckboxTable.setAscending(!ascending);
        checkboxTableViewer.refresh();
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub

    }
}
