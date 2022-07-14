/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup.tabledescriptorlist;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.entry.sample.config.Config;
import org.grits.toolbox.entry.sample.model.Descriptor;


/**
 * 
 *
 */
public class DescriptorListLabelProvider implements ITableLabelProvider
{

    @Override
    public void addListener(ILabelProviderListener listener)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isLabelProperty(Object element, String property)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex)
    {
        String result = null;
        if(element instanceof Descriptor)
        {
            Descriptor desc = (Descriptor) element;
            switch (columnIndex) {
            case 0:
                break;
            case 1 :
                result = desc.getLabel();
                break;
            case 2 :
                if(desc.getMaxOccurrence()!= null)
                    result = desc.getMaxOccurrence() + "";
                else
                    result = Config.MAX_OCCURRENCE_UNBOUNDED;
                break;
            }
        }
        return result;
    }

}
