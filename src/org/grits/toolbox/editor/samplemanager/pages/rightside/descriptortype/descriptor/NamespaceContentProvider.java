/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.grits.toolbox.entry.sample.model.Descriptor;

/**
 * 
 *
 */
public class NamespaceContentProvider implements IStructuredContentProvider
{

    @Override
    public void dispose()
    {
        
    }


    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        
    }

    @Override
    public Object[] getElements(Object inputElement)
    {
        if(inputElement instanceof Descriptor) //namespace tableViewer
        {
            return ((Descriptor) inputElement).getNamespaces().toArray();
        }
        else if(inputElement instanceof List) // list of available namespaces in combo
        {
            return ((List<?>) inputElement).toArray();
        }
        return null;
    }

}
