/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.category;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class CategoryMemberLabelProvider implements ITableLabelProvider
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
            case 0 :
                result = desc.getLabel();
                break;
            case 1 :
                if(desc.getMaxOccurrence()!= null)
                    result = desc.getMaxOccurrence() + "";
                else
                    result = "Unbounded / Infinite";
                break;
            }
        }
        if(element instanceof DescriptorGroup)
        {
            DescriptorGroup desc = (DescriptorGroup) element;
            switch (columnIndex) {
            case 0 :
                result = desc.getLabel();
                break;
            case 1 :
                if(desc.getMaxOccurrence()!= null)
                    result = desc.getMaxOccurrence() + "";
                else
                    result = "Unbounded / Infinite";
                break;
            }
        }
        return result;
    }

}
