/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;
import org.grits.toolbox.entry.sample.utilities.UtilityDescriptorDescriptorGroup;

/**
 * 
 *
 */
public class MandatoryCheckTemplateDescriptorGroupProvider implements
        ICheckStateProvider
{
    private CheckboxTableViewer tableViewer;

    public MandatoryCheckTemplateDescriptorGroupProvider(CheckboxTableViewer tableViewer)
    {
        this.tableViewer = tableViewer;
    }

    @Override
    public boolean isChecked(Object element)
    {
        List<String> mandatoryURIs = new ArrayList<String>();
        if(tableViewer.getInput() instanceof CategoryTemplate)
        {
            CategoryTemplate categoryTemplate = (CategoryTemplate) tableViewer.getInput();
            mandatoryURIs = UtilityDescriptorDescriptorGroup.getMandatoryDescriptorGroupURIs(categoryTemplate);
        }
        else if(tableViewer.getInput() instanceof List)
        {
//            @SuppressWarnings("unchecked")
//            List<CategoryTemplate> categoryTemplates = (List<CategoryTemplate>) leftSideTableViewer.getInput();
//            for(CategoryTemplate categoryTemplate : categoryTemplates)
//            {
//                mandatoryURIs.addAll(UtilityDescriptorDescriptorGroup.getMandatoryDescriptorURIs(categoryTemplate));
//            }
        }
        DescriptorGroup dg = (DescriptorGroup) element;
        return mandatoryURIs.contains(dg.getUri());
    }

    @Override
    public boolean isGrayed(Object element)
    {
        return false;
    }
}
