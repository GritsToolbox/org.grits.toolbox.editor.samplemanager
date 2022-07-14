/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public class CheckboxSelectionListener extends AbstractDescriptorTypePartsListener implements SelectionListener
{
    private String categoryUri = null;

    public CheckboxSelectionListener(AbstractManagementPage page, String category)
    {
        super(page);
        this.categoryUri = category;
    }

    @Override
    public void widgetSelected(SelectionEvent e)
    {
        boolean selected = ((Button) e.getSource()).getSelection();
        boolean prevSelection = selectedObject.getCategories() != null
                && selectedObject.getCategories().contains(categoryUri);
        if(selected^prevSelection)
        {
            this.selectedObject.updateCategorySelection(categoryUri, selected);
            this.update(selected);
        }
    }

    protected void updateDataSourcePart(Object selected)
    {
        ontoManagerApi.updateCategorySelection(categoryUri, this.selectedObject.getUri(), (boolean) selected);
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {

    }


}
