/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.dialogs.addunit;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.hp.hpl.jena.ontology.OntClass;

/**
 * 
 *
 */
public class MeasurementUnitContentProvider implements
        ITreeContentProvider
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
        //expected input is a single Ontclass (unit class) from the ontology
        return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement)
    {
        Object[] classes = null;
        if(parentElement instanceof OntClass)
        {
            List<?> list = ((OntClass) parentElement).listSubClasses(true).toList();
            classes = new OntClass[list.size()];
            int i = 0;
            for(Object classInstance : list)
            {
                if(classInstance instanceof OntClass)
                {
                    classes[i++] = classInstance;
                }
            }
        }
        return classes;
    }

    @Override
    public Object getParent(Object element)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasChildren(Object element)
    {
        if(element instanceof OntClass)
        {
            return !((OntClass) element).listSubClasses(true).toList().isEmpty();
        }
        return false;
    }

}
