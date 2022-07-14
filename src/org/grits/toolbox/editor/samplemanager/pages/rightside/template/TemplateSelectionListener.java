/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template;

import org.apache.log4j.Logger;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;

/**
 * 
 *
 */
public class TemplateSelectionListener extends RightSideSelectionListener
{
    private Logger logger = Logger.getLogger(TemplateSelectionListener.class);

    public TemplateSelectionListener(AbstractManagementPage page)
    {
        rightSideSection  = new RightSideTemplateSection(page);
    }

    @Override
    protected void setSelection(ClassesWithFeatures selectedObject)
    {
        logger.debug("- START : Selecting Template in Template page.");

        ((RightSideTemplateSection) rightSideSection).setSelectedObject(selectedObject);

        logger.debug("- END   : Selecting Template in Template page.");
    }
}
