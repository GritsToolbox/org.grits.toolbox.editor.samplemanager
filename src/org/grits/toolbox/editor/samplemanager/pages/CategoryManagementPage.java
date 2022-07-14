/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.grits.toolbox.core.utilShare.ErrorUtils;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.general.ListLabelProvider;
import org.grits.toolbox.editor.samplemanager.pages.leftside.SetupLeftSideSection;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.category.CategorySelectionListener;
import org.grits.toolbox.editor.samplemanager.util.CategoryObjectComparator;
import org.grits.toolbox.entry.sample.model.Category;

/**
 * 
 *
 */
public class CategoryManagementPage extends AbstractManagementPage
{
    private Logger logger = Logger.getLogger(CategoryManagementPage.class);

    public static final String ID = "3";
    public static final String PAGE_TITLE = "Categories";
    private List<Category> inputCategories = null;
    public HashMap<String, String> uriLabelMap;

    public CategoryManagementPage(ManagementEditor managementEditor)
    {
        super(managementEditor, ID, PAGE_TITLE);
        this.inputCategories  = this.getAllCategories();
    }

    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<Category>();
        logger.debug("- START : Retrieving all Categories for the Category Page.");
        try
        {
            categories = ((ManagementEditor) this.getEditor()).getOntologyManagerApi().getAllCategories();
        } catch (Exception ex)
        {
        	ErrorUtils.createErrorMessageBox(Display.getCurrent().getActiveShell(), "Error Reading Ontology", ex); 
            logger.error(ex);
        }
        logger.debug("- END   : Retrieving all Categories for the Category Page.");
        return categories;
    }

    @Override
    public void createRestOftheContent()
    {
        logger.debug("- START : Creating remaining part for the Category page.");
        try
        {
            leftSectionOfPage.setText(this.getTitle());
            SetupLeftSideSection leftSectionSetup = new SetupLeftSideSection(this, getInput());
            leftSideTableViewer = leftSectionSetup.tableViewer;
            uriLabelMap = ((ManagementEditor) this.getEditor()).getAllCategoriesURILabelMap();
            ((ListLabelProvider) leftSideTableViewer.getLabelProvider()).setCategoryHashMap(uriLabelMap);
            Collections.sort(inputCategories, new CategoryObjectComparator());
            leftSideTableViewer.setInput(getInput());
            leftSideTableViewer.addSelectionChangedListener(getSelectionListener());
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Creating remaining part for the Category page.");
    }

    protected Object getInput()
    {
        return this.inputCategories;
    }

    protected void sortLeftSideInput()
    {
        
    }

    @Override
    protected RightSideSelectionListener getSelectionListener()
    {
        return new CategorySelectionListener(this, uriLabelMap);
    }

    @Override
    protected void addInputToList(List<ClassesWithFeatures> inputList,
            int selectionIndex, String label, String uri)
    {
        // no add button for cateogry
    }
    @Override
    public void refreshPage()
    {
        leftSideSelection = Math.max(leftSideTableViewer.getTable().getSelectionIndex(), 0);
        this.inputCategories = this.getAllCategories();
        Collections.sort(inputCategories, new CategoryObjectComparator());
        leftSideTableViewer.setInput(this.inputCategories);
        if(leftSideTableViewer.getTable().getItemCount() > 0)
        {
            leftSideTableViewer.getTable().select(leftSideSelection);
            leftSideTableViewer.setSelection(leftSideTableViewer.getSelection());
        }
    }


}
