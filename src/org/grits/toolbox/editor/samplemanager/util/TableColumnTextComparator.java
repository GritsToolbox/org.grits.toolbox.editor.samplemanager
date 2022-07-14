/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.util;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

/**
 * 
 *
 */
public class TableColumnTextComparator extends ViewerComparator
{
	public int compare(Viewer viewer, Object object1, Object object2)
	{
		if(viewer instanceof TableViewer)
		{
			TableViewer tableViewer = (TableViewer) viewer;
			IBaseLabelProvider baseLabelProvider = tableViewer.getLabelProvider();
			if(baseLabelProvider instanceof ITableLabelProvider)
			{
				ITableLabelProvider labelProvider = (ITableLabelProvider) baseLabelProvider;
				TableColumn sortColumn = tableViewer.getTable().getSortColumn();
				int sortColumnIndex = sortColumn == null 
						? 0 : tableViewer.getTable().indexOf(sortColumn);
				String label1 = labelProvider.getColumnText(object1, sortColumnIndex);
				String label2 = labelProvider.getColumnText(object2, sortColumnIndex);
				int comparision = label1.compareToIgnoreCase(label2);
				return tableViewer.getTable().getSortDirection() == SWT.UP ?
						comparision : -comparision;
			}
		}
		return 0;
	}
}
