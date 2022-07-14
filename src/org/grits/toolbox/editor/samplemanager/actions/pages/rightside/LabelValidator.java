/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside;

import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * 
 *
 */
public class LabelValidator implements IInputValidator
{
    private List<String> existingLabels;
    public LabelValidator(List<String> existingLabels)
    {
        this.existingLabels = existingLabels;
    }
    
    /**
     * @param existingLabels the existingLabels to set
     */
    public void setExistingLabels(List<String> existingLabels)
    {
        this.existingLabels = existingLabels;
    }


    @Override
    public String isValid(String newText)
    {
        newText = newText.trim();
        if(newText.isEmpty())
        {
            return "";
        }
        else if(existingLabels.contains(newText))
        {
            return "This label already exists. Please try another label";
        }
        return null;
    }
}

