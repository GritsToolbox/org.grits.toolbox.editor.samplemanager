/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class DescriptorGroupLabelProvider implements ITableLabelProvider
{

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    @Override
    public void addListener(ILabelProviderListener listener)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean isLabelProperty(Object element, String property)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    @Override
    public void removeListener(ILabelProviderListener listener)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    @Override
    public Image getColumnImage(Object element, int columnIndex)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex)
    {
        String result = null;
        if(element instanceof DescriptorGroup)
        {
            DescriptorGroup dg = (DescriptorGroup) element;
            switch (columnIndex) {
            case 0:
                break;
            case 1 :
                result = dg.getLabel();
                break;
            case 2 :
                if(dg.getMaxOccurrence()!= null)
                    result = dg.getMaxOccurrence() + "";
                else
                    result = "Unbounded / Infinite";
                break;
            }
        }
        return result;
    }

}
