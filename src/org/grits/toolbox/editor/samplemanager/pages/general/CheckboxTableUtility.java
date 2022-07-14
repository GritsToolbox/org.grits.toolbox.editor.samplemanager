/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.general;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup.tabledescriptorlist.DescriptorListContentProvider;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup.tabledescriptorlist.DescriptorListLabelProvider;

/**
 * 
 *
 */
public class CheckboxTableUtility
{

	public static CheckboxTableViewer createCheckboxTableViewer(AbstractManagementPage page, 
			Composite descriptionListSection, int swtStyle, String columnName)
	{

		FormToolkit toolkit = page.getToolkit();
		Table table = toolkit.createTable(descriptionListSection, 
				SWT.FILL | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION|swtStyle);
		int i =0;
		int totalWidth = 0;
		int firstColumnWidth = 90;
		int secondColumnWidth = 120;
		int finalWidth = 726;
		CheckboxTableViewer tableViewer = new CheckboxTableViewer(table);
		TableViewerColumn columnViewer = null;
		if(swtStyle == SWT.CHECK)
		{
			columnViewer = new TableViewerColumn(tableViewer, SWT.RIGHT, i);
			columnViewer.getColumn().setText("Mandatory");
			columnViewer.getColumn().setWidth(firstColumnWidth);
			i++;
			totalWidth += firstColumnWidth;
			totalWidth += secondColumnWidth;
		}

		columnViewer = new TableViewerColumn(tableViewer, SWT.LEFT, i);
		columnViewer.getColumn().setText(columnName);
		columnViewer.getColumn().setWidth(finalWidth-totalWidth);
		i++;

		if(swtStyle == SWT.CHECK)
		{
			columnViewer = new TableViewerColumn(tableViewer, SWT.CENTER, i);
			columnViewer.getColumn().setText("Max Occurrence");
			columnViewer.getColumn().setWidth(secondColumnWidth);
			columnViewer.setEditingSupport(new MaxOccurrenceEditingSupport(page, tableViewer));
		}
		//        column = new TableColumn(table, SWT.CENTER, 3);
		//        column.setText("");
		//        column.setWidth(250);

		tableViewer.setContentProvider(new DescriptorListContentProvider());
		tableViewer.setLabelProvider(new DescriptorListLabelProvider());
		TableWrapData tableWrapData = new TableWrapData();
		tableWrapData.grabHorizontal = true;
		tableWrapData.colspan = 4;
		tableWrapData.heightHint = 150;
		tableViewer.getTable().setLayoutData(tableWrapData);
		tableViewer.getTable().setHeaderVisible(true);
		return tableViewer;
	}

}
