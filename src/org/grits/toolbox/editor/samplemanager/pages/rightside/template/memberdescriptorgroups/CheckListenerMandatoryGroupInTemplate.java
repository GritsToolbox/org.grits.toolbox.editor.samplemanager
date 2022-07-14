/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class CheckListenerMandatoryGroupInTemplate extends 
AbstractAddAction implements ICheckStateListener
{

    public CheckListenerMandatoryGroupInTemplate(AbstractManagementPage page, CheckboxTableViewer tableViewer)
    {
        super(page, tableViewer);
    }

    @Override
    public void checkStateChanged(CheckStateChangedEvent event)
    {
        boolean checked = event.getChecked();
        DescriptorGroup descriptorGroup = (DescriptorGroup) event.getElement();
        CheckboxTableViewer tableViewer = (CheckboxTableViewer) event.getSource();
        if(tableViewer.getInput() instanceof CategoryTemplate){
            CategoryTemplate categoryTemplate = (CategoryTemplate) tableViewer.getInput();
            if(checked)
            {
                categoryTemplate.getOptionalDescriptorGroups().remove(descriptorGroup);
                categoryTemplate.getMandatoryDescriptorGroups().add(descriptorGroup);
            }
            else
            {
                categoryTemplate.getMandatoryDescriptorGroups().remove(descriptorGroup);
                categoryTemplate.getOptionalDescriptorGroups().add(descriptorGroup);
            }
            this.updateOntology(categoryTemplate.getTemplateURI(), categoryTemplate.getUri(),descriptorGroup.getUri(), checked);
            this.refreshModifed();
        }

    }

    private void updateOntology(String templateURI, String categoryURI, String descriptorGroupURI, boolean mandatory)
    {
        this.ontologyManagerApi.setMandatoryStateDescriptorInCategoryTemplate(templateURI, categoryURI, descriptorGroupURI, mandatory);
    }

    @Override
    public void run()
    {

    }
}
