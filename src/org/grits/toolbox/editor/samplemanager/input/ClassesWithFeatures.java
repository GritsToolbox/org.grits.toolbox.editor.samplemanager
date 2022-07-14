/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.input;

/**
 * 
 *
 */
public abstract class ClassesWithFeatures
{
    protected boolean editable = false;

    /**
     * @return the editable
     */
    public boolean isEditable()
    {
        return editable ;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }
    
    public abstract String getUri();
    
    public abstract String getLabel();
    
    public abstract void setLabel(String label);
    
    public abstract String getDescription();
 
    public abstract void setDescription(String description);

}