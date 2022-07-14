/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.category;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;
import org.grits.toolbox.entry.sample.model.Category;

/**
 * 
 *
 */
public class CategorySelectionListener extends RightSideSelectionListener
{
    private Logger logger = Logger.getLogger(CategorySelectionListener.class);

    public CategorySelectionListener(AbstractManagementPage page, HashMap<String, String> uriLabelMap)
    {
        rightSideSection  = new RightSideCategorySection(page, uriLabelMap);
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event)
    {
        logger.debug("- START : Selecting Category in Category page.");

        TableViewer tableViewer = (TableViewer) event.getSource();
        Category selectedObject = (Category) tableViewer
                .getElementAt(tableViewer.getTable().getSelectionIndex());

        ((RightSideCategorySection) rightSideSection).setSelectedObject(selectedObject);

        logger.debug("- END   : Selecting Category in Category page.");
    }

    @Override
    protected void setSelection(ClassesWithFeatures selectedObject)
    {
        
    }
}
