/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class DescriptorGroupMembersContenProvider implements
        IStructuredContentProvider
{

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    @Override
    public Object[] getElements(Object inputElement)
    {
        List<Descriptor> descriptors = new ArrayList<Descriptor>();
        if(inputElement instanceof DescriptorGroup)
        {
            DescriptorGroup dg = (DescriptorGroup) inputElement;
            descriptors.addAll(dg.getMandatoryDescriptors());
            descriptors.addAll(dg.getOptionalDescriptors());
        }
        return descriptors.toArray();
    }

}
