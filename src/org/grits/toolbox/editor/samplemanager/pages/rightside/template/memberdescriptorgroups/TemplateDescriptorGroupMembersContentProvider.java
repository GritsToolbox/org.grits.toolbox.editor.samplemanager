/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class TemplateDescriptorGroupMembersContentProvider implements
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

        List<DescriptorGroup> descriptorGroups = new ArrayList<DescriptorGroup>();
        if(inputElement instanceof CategoryTemplate)
        {
            CategoryTemplate categoryTemplate = (CategoryTemplate) inputElement;
            descriptorGroups.addAll(categoryTemplate.getMandatoryDescriptorGroups());
            descriptorGroups.addAll(categoryTemplate.getOptionalDescriptorGroups());
        }
//        else if(inputElement instanceof List)
//        {
//            Iterator<?> iterator = ((List<?>) inputElement).iterator();
//            CategoryTemplate categoryTemplate = null;
//            while(iterator.hasNext())
//            {
//                categoryTemplate = (CategoryTemplate) iterator.next();
//                descriptorGroups.addAll(categoryTemplate.getMandatoryDescriptorGroups());
//                descriptorGroups.addAll(categoryTemplate.getOptionalDescriptorGroups());
//            }
//        }
        return descriptorGroups.toArray();
    }

}
