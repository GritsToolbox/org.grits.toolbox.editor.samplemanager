/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.general;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public class GeneralizedTablesWithActions
{
    private Section section;
    private FormToolkit toolkit;
    private ToolBarManager toolBarManager;

    public GeneralizedTablesWithActions(AbstractManagementPage managementPage,  Section section)
    {
        this.toolkit = managementPage.getToolkit();
        this.section = section;
    }
    
    public TableViewer createTableViewer(int widthHint, int heightHint)
    {
        Composite leftSectionComposite = toolkit.createComposite(section, SWT.NONE);
        leftSectionComposite.setLayout(new GridLayout());
        TableViewer tableViewer = new TableViewer(toolkit.createTable(leftSectionComposite,SWT.LINE_SOLID | SWT.SINGLE | SWT.FILL | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION));
        GridData tableViewerData = new GridData();
        tableViewerData.widthHint = widthHint;
        tableViewerData.heightHint = heightHint;
        tableViewerData.grabExcessHorizontalSpace = true;
        tableViewer.getTable().setLayoutData(tableViewerData);
        tableViewer.getTable().setVisible(true);
//        leftSideTableViewer.getList().setLinesVisible(true);
        leftSectionComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        section.setClient(leftSectionComposite);
        toolBarManager = new ToolBarManager(SWT.BALLOON);
        ToolBar toolbar = toolBarManager.createControl(section);
        section.setTextClient(toolbar);
        return tableViewer;
    }

    public ToolBarManager getToolBarManager()
    {
        return toolBarManager;
    }
    

//    protected IAction getSortListAction(TableViewer tableViewer)
//    {
//        return new SortClassesWithFeatures(tableViewer);
//        
//    }

//    private IAction getAddToListAction(TableViewer leftSideTableViewer)
//    {
//        
//        return addAction;
//    }
    

//    public void setDeleteFromListAction(IAction deleteAction)
//    {
//        
//        toolBarManager.update(true);
//    }

//    public void setAddToListAction(IAction addAction)
//    {
//        
//        toolBarManager.update(true);
//    }
}
