/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.entry.sample.model.Namespace;

/**
 * 
 *
 */
public class NamespaceComboLabelProvider implements ILabelProvider
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
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener)
    {

    }

    @Override
    public Image getImage(Object element)
    {
        return null;
    }

    @Override
    public String getText(Object element)
    {
        String label = null;
        if(element instanceof Namespace)
        {
            label = ((Namespace) element).getLabel();
        }
        return label;
    }

}
