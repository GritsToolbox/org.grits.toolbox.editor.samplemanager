/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.leftside;


import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.core.utilShare.provider.GenericListContentProvider;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.general.GeneralizedTablesWithActions;
import org.grits.toolbox.editor.samplemanager.pages.general.ListLabelProvider;

/**
 * 
 *
 */
public class SetupLeftSideSection
{
    private GeneralizedTablesWithActions generalizedTableWithActions;
    private Object inputList;
    public TableViewer tableViewer;

    public SetupLeftSideSection(AbstractManagementPage managementPage, Object inputList)
    {
        this.generalizedTableWithActions = new GeneralizedTablesWithActions(managementPage, managementPage.getLeftSectionOfPage());
        this.inputList = inputList;
        this.setUp();
    }

    public void setUp()
    {
        tableViewer = this.generalizedTableWithActions.createTableViewer(300, 730);
        tableViewer.setLabelProvider(new ListLabelProvider());
        tableViewer.setContentProvider(new GenericListContentProvider());
        tableViewer.setInput(this.inputList);
    }

}
