/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.Descriptor;

/**
 * 
 *
 */
public class TemplateDescriptorMembersContentProvider implements
IStructuredContentProvider
{

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Object[] getElements(Object inputElement)
    {
        List<Descriptor> descriptors = new ArrayList<Descriptor>();
        if(inputElement instanceof CategoryTemplate)
        {
            CategoryTemplate categoryTemplate = (CategoryTemplate) inputElement;
            descriptors.addAll(categoryTemplate.getMandatoryDescriptors());
            descriptors.addAll(categoryTemplate.getOptionalDescriptors());
        }
//        else if(inputElement instanceof List)
//        {
//            Iterator<?> iterator = ((List<?>) inputElement).iterator();
//            CategoryTemplate categoryTemplate = null;
//            while(iterator.hasNext())
//            {
//                categoryTemplate = (CategoryTemplate) iterator.next();
//                descriptors.addAll(categoryTemplate.getMandatoryDescriptors());
//                descriptors.addAll(categoryTemplate.getOptionalDescriptors());
//            }
//        }
        return descriptors.toArray();
    }
}
