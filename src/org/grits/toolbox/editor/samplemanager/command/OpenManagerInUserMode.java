/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.command;

import java.util.Arrays;
import java.util.List;

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
public class OpenManagerInUserMode extends AbstractHandler
{
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {        
        logger.debug("START COMMAND : Open Meta Data Manager Editor in User Mode...");
        try {

            String filePath1 = null;
            try{
                filePath1 = UtilityOntologyLocation.getStandardOntologyLocation();
            } catch (Exception ex)
            {
                logger .error("Error getting the standard ontology path for the editor in User Mode.");
                throw ex;
            }
            String filePath2 = null;
            try {
                filePath2 = UtilityOntologyLocation.getLocalOntologyLocation();
            } catch (Exception ex)
            {
                logger .error("Error getting the local ontology path for the editor in User Mode.");
                throw ex;
            }

            List<String> filenames = Arrays.asList(filePath1, filePath2);
            ManagementEditorInput editorInput = new ManagementEditorInput(filenames);
            try{
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
                        editorInput, ManagementEditor.EDITOR_ID);
            } catch (PartInitException ex)
            {
                logger .error("Error opening the Manager Editor in User Mode.");
                throw ex;
            }
        } catch (Exception ex)

        {
            logger .fatal("Error opening the manager editor in User Mode.", ex);
        }
        logger.debug("...END COMMAND : Open Meta Data Manager Editor in User Mode.");
        return null;
    }

}
