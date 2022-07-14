/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 *
 */
public class CategoryComboContentProvider implements IStructuredContentProvider
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

    @Override
    public Object[] getElements(Object inputElement)
    {
        return ((List<?>) inputElement).toArray();
    }

}
