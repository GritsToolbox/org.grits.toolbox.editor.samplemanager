/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.command;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.input.ManagementEditorInput;
import org.grits.toolbox.entry.sample.utilities.UtilityOntologyLocation;

/**
 * 
 *
 */
public class OpenManagerInAdminMode extends AbstractHandler
{
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        logger.debug("START COMMAND : Open Meta Data Manager Editor in Amin Mode...");
        try {
            String filePath = UtilityOntologyLocation.getStandardOntologyLocation();
            ManagementEditorInput editorInput = new ManagementEditorInput(filePath);
            try{
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
                        editorInput, ManagementEditor.EDITOR_ID);
            } catch (PartInitException ex)
            {
                logger .error("Error opening the Manager Editor in Admin Mode.");
                throw ex;
            }
        } catch (Exception e1)
        {
            logger .error("Error opening the editor with the input file in Admin Mode.");
        }
        logger.debug("...END COMMAND : Open Meta Data Manager Editor in Amin Mode.");
        return null;
    }

}
