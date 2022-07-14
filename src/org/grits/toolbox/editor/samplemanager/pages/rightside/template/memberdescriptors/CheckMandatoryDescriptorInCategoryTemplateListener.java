/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.Descriptor;

/**
 * 
 *
 */
public class CheckMandatoryDescriptorInCategoryTemplateListener extends
AbstractAddAction implements ICheckStateListener
{

    public CheckMandatoryDescriptorInCategoryTemplateListener(
            AbstractManagementPage page, TableViewer tableViewer)
    {
        super(page, tableViewer);
    }

    @Override
    public void checkStateChanged(CheckStateChangedEvent event)
    {
        boolean checked = event.getChecked();
        Descriptor descriptor = (Descriptor) event.getElement();
        CheckboxTableViewer tableViewer = (CheckboxTableViewer) event.getSource();
        if(tableViewer.getInput() instanceof CategoryTemplate){
            CategoryTemplate categoryTemplate = (CategoryTemplate) tableViewer.getInput();
            if(checked)
            {
                categoryTemplate.getOptionalDescriptors().remove(descriptor);
                categoryTemplate.getMandatoryDescriptors().add(descriptor);
            }
            else
            {
                categoryTemplate.getMandatoryDescriptors().remove(descriptor);
                categoryTemplate.getOptionalDescriptors().add(descriptor);
            }
            this.updateOntology(categoryTemplate.getTemplateURI(), categoryTemplate.getUri(),descriptor.getUri(), checked);
            this.refreshModifed();
        }

    }

    private void updateOntology(String templateURI, String categoryURI, String descriptorURI, boolean mandatory)
    {
        this.ontologyManagerApi.setMandatoryStateDescriptorInCategoryTemplate(templateURI, categoryURI, descriptorURI, mandatory);
    }

    @Override
    public void run()
    {

    }

}
