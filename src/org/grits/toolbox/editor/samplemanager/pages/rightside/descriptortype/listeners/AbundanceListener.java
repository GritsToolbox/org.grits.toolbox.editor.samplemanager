/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Spinner;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public class AbundanceListener extends AbstractDescriptorTypePartsListener implements ModifyListener
{
    public AbundanceListener(AbstractManagementPage page)
    {
        super(page);
    }

    @Override
    public void modifyText(ModifyEvent e)
    {
        Integer maxOccurrence = ((Spinner) e.getSource()).getSelection();
        Integer oldValue = this.selectedObject.getMaxOccurrence();
        boolean notSame = oldValue == null && maxOccurrence.intValue() != 0;
        notSame = notSame || (oldValue!=null && (maxOccurrence.intValue()!=oldValue.intValue()));
        if(notSame)
        {
            if(maxOccurrence.intValue() == 0)
                maxOccurrence = null;
            this.selectedObject.setMaxOccurrence(maxOccurrence);
            this.update(maxOccurrence);
        }
    }

    protected void updateDataSourcePart(Object maxOccurrence)
    {
        ontoManagerApi.updateMaxOccurrence(this.selectedObject.getUri(), (Integer) maxOccurrence);
    }
}
