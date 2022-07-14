/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.entry.sample.model.Namespace;

/**
 * 
 *
 */
public class NamespaceTableLabelProvider implements ITableLabelProvider
{
    @Override
    public void addListener(ILabelProviderListener listener)
    {
        
    }

    @Override
    public void dispose()
    {
        
    }

    @Override
    public boolean isLabelProperty(Object element, String property)
    {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener)
    {
        
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex)
    {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex)
    {
        if(element instanceof Namespace)
        {
            switch(columnIndex)
            {
            case 0 :
                return  ((Namespace) element).getLabel();
            case 1 :
                return  ((Namespace) element).getUri();
            }
        }
        return null;
    }

}
