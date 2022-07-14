/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.grits.toolbox.editor.samplemanager.actions.RefreshPageAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.CreateNewObjectInOntology;
import org.grits.toolbox.editor.samplemanager.actions.pages.DeleteObjectFromOntology;
import org.grits.toolbox.editor.samplemanager.actions.pages.sorting.SortClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.config.Config;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.leftside.SetupLeftSideSection;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;

/**
 * 
 *
 */
public abstract class AbstractManagementPage extends FormPage
{
    private Logger logger = Logger.getLogger(AbstractManagementPage.class);

    protected Section leftSectionOfPage = null;
    private Section rightSectionOfPage = null;
    private FormToolkit toolkit = null;
    protected TableViewer leftSideTableViewer = null;
    protected SortClassesWithFeatures sortAlphaNumericAction = null;

    protected int leftSideSelection = 0;

    private Composite rightComposite;

    public AbstractManagementPage(FormEditor editor, String id, String title)
    {
        super(editor, id, title);
    }

    /**
     * @return the leftSectionOfPage
     */
    public Section getLeftSectionOfPage()
    {
        return leftSectionOfPage;
    }

    public Composite getRightComposite()
    {
        return rightComposite;
    }

    /**
     * @return the rightSectionOfPage
     */
    public Section getRightSectionOfPage()
    {
        return rightSectionOfPage;
    }

    /**
     * @return the toolkit
     */
    public FormToolkit getToolkit()
    {
        return toolkit;
    }

    public TableViewer getLeftSideTableViewer()
    {
        return leftSideTableViewer;
    }

    @Override
    protected void createFormContent(IManagedForm managedForm)
    {
        logger.debug("- START : Creating Abstract page.");
        try
        {
            super.createFormContent(managedForm);
            ScrolledForm form = managedForm.getForm();
            form.getForm().getHead().setBackground(Display.getCurrent().getSystemColor(
                    SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

            managedForm.getForm().setText(this.getTitle());
            toolkit = managedForm.getToolkit();

            Composite fullComposite = form.getBody();
            fullComposite.setLayout(new GridLayout(2, false));

            Composite leftComposite = toolkit.createComposite(fullComposite);
            TableWrapLayout leftCompositeLayout = new TableWrapLayout();
            leftCompositeLayout.numColumns = 1;
            leftCompositeLayout.makeColumnsEqualWidth = true;
            leftComposite.setLayout(leftCompositeLayout);

            leftSectionOfPage = toolkit.createSection(leftComposite , Section.TITLE_BAR);
            leftSectionOfPage.setLayout(new TableWrapLayout());

            TableWrapData section1LayoutData = new TableWrapData();
            section1LayoutData.colspan = 1;
            section1LayoutData.grabVertical = true;
            leftSectionOfPage.setLayoutData(section1LayoutData);


            GridData leftCompositeData = new GridData();
            leftCompositeData.grabExcessVerticalSpace = true;
            leftCompositeData.horizontalSpan = 1;
            leftCompositeData.horizontalAlignment = GridData.BEGINNING;
            leftCompositeData.widthHint = 200;
            leftCompositeData.minimumHeight = 800;
            leftComposite.setLayoutData(leftCompositeData);

            rightComposite = toolkit.createComposite(fullComposite);
            GridLayout rightCompositeLayout = new GridLayout();
            rightCompositeLayout.numColumns = 1;
            rightCompositeLayout.makeColumnsEqualWidth = true;
            rightComposite.setLayout(rightCompositeLayout);

            rightSectionOfPage  = toolkit.createSection(rightComposite , Section.TITLE_BAR);
            rightSectionOfPage.setText(Config.MANAGEMENT_RIGHT_SIDE_SECTION_NO_SELECTION_TITLE);
            rightSectionOfPage.setLayout(new TableWrapLayout());

//            TableWrapData section2LayoutData = new TableWrapData();
//            section2LayoutData.colspan = 1;
//            section2LayoutData.grabVertical = true;
            GridData rightSectionLayoutData = new GridData();
            rightSectionLayoutData.horizontalSpan = 1;
//            rightSectionLayoutData.grabExcessVerticalSpace = true;
            rightSectionOfPage.setLayoutData(rightSectionLayoutData);


            GridData rightCompositeData = new GridData();
            rightCompositeData.grabExcessVerticalSpace = true;

            rightCompositeData.grabExcessHorizontalSpace = true;
            rightCompositeData.horizontalSpan = 1;
            rightCompositeData.horizontalAlignment = GridData.BEGINNING;
            rightCompositeData.minimumHeight = 807;
//            rightCompositeData.minimumWidth = 700;
            rightComposite.setLayoutData(rightCompositeData);

            leftSectionOfPage.setText(this.getTitle());
            SetupLeftSideSection leftSectionSetup = new SetupLeftSideSection(this, getInput());
            leftSideTableViewer = leftSectionSetup.tableViewer;
            leftSideTableViewer.addSelectionChangedListener(getSelectionListener());

            this.createRestOftheContent();

            IToolBarManager manager = form.getToolBarManager();
            manager.add(getRefreshPageAction());
            form.updateToolBar();
            sortLeftSideInput();
            this.selectDefault();
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Creating Abstract page.");
    }

    protected void sortLeftSideInput()
    {
        sortAlphaNumericAction.run();
    }

    protected void selectDefault()
    {
        if(leftSideTableViewer.getTable().getItemCount() > 0)
        {
            leftSideTableViewer.getTable().setSelection(0);
            leftSideTableViewer.setSelection(leftSideTableViewer.getSelection());
        }
    }

    private IAction getRefreshPageAction()
    {
        return new RefreshPageAction(this);
    }

    public abstract void createRestOftheContent();

    protected void createToolBar(TableViewer tableViewer, String classUri)
    {
        logger.debug("- START : Creating Toolbar for the page.");
        try
        {
            ToolBarManager toolBarManager = new ToolBarManager();
            toolBarManager.add(new CreateNewObjectInOntology(this, tableViewer, classUri));
            toolBarManager.add(new DeleteObjectFromOntology(this, tableViewer));
            sortAlphaNumericAction = new SortClassesWithFeatures(tableViewer);
            toolBarManager.add(sortAlphaNumericAction);

            ToolBar toolbar = toolBarManager.createControl(leftSectionOfPage);
            leftSectionOfPage.setTextClient(toolbar);
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Creating Toolbar for the page.");
    }

    protected abstract RightSideSelectionListener getSelectionListener();

    public abstract void refreshPage();
//    {
//        ManagementEditor editor = ((ManagementEditor) getEditor());
//        editor.reLoadPage(getIndex());
//        editor.beingReloaded = true;
//        editor.setActivePage(getId());
//    }

    protected abstract Object getInput();

    @SuppressWarnings("unchecked")
    public void addInput(String label, String uri)
    {
        logger.debug("- START : Adding a new input to the page.");
        try
        {
            List<ClassesWithFeatures> inputList = (List<ClassesWithFeatures>) leftSideTableViewer.getInput();
            int selectionIndex = Math.max(leftSideTableViewer.getTable().getSelectionIndex(), 0);
            addInputToList(inputList, selectionIndex, label, uri);
            leftSideTableViewer.refresh();
            leftSideTableViewer.getTable().select(selectionIndex);
            leftSideTableViewer.setSelection(leftSideTableViewer.getSelection());
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Adding a new input to the page.");
    }

    protected abstract void addInputToList(List<ClassesWithFeatures> inputList, 
            int selectionIndex, String label, String uri);


}
