/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.dialogs.addunit;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.hp.hpl.jena.ontology.OntClass;

/**
 * 
 *
 */
public class MeasurementUnitLabelProvider implements ILabelProvider
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
    public Image getImage(Object element)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getText(Object element)
    {
        if(element instanceof OntClass)
        {
            return ((OntClass) element).getLabel(null);
        }
        return null;
    }

}
