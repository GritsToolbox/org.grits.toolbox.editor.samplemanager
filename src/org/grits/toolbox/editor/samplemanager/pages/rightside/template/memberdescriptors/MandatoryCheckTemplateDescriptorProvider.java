/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.utilities.UtilityDescriptorDescriptorGroup;

/**
 * 
 *
 */
public class MandatoryCheckTemplateDescriptorProvider implements
ICheckStateProvider
{
    private CheckboxTableViewer tableViewer;

    public MandatoryCheckTemplateDescriptorProvider(CheckboxTableViewer tableViewer)
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
            mandatoryURIs = UtilityDescriptorDescriptorGroup.getMandatoryDescriptorURIs(categoryTemplate);
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
        Descriptor desc = (Descriptor) element;
        return mandatoryURIs.contains(desc.getUri());
    }

    @Override
    public boolean isGrayed(Object element)
    {
        return false;
    }

}
