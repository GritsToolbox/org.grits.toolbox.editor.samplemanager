/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public class DescriptionModifyListener extends AbstractModificationListener implements ModifyListener, KeyListener 
{
    public DescriptionModifyListener(AbstractManagementPage page)
    {
        super(page);
    }

    @Override
    public void modifyText(ModifyEvent e)
    {
        String newDescription = ((Text) e.getSource()).getText().trim();
        boolean changeInValue = this.selectedObject.getDescription() == null && (newDescription != null && !newDescription.isEmpty());
        changeInValue = changeInValue || (this.selectedObject.getDescription()!= null && (!this.selectedObject.getDescription().equals(newDescription)));
        if(changeInValue)
        {
            this.selectedObject.setDescription(newDescription);
            this.update(newDescription);
        }
    }

    protected void updateDataSourcePart(Object newDescription)
    {
        ontoManagerApi.updateDescription(this.selectedObject.getUri(), (String) newDescription);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.stateMask == SWT.CTRL && e.keyCode == 'a')
            ((Text) e.getSource()).selectAll();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub
        
    }

}
