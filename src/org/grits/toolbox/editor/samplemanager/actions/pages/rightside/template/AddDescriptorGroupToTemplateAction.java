/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.dialogs.DescriptorGroupSelectionDialog;
import org.grits.toolbox.editor.samplemanager.input.DescriptorGroupWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.DescriptorGroupManagementPage;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;
import org.grits.toolbox.entry.sample.utilities.UtilityDescriptorDescriptorGroup;

/**
 * 
 *
 */
public class AddDescriptorGroupToTemplateAction extends AbstractAddAction
{
    private ArrayList<DescriptorGroup> descriptorGroups;
    private HashMap<String, String> descriptorLabelToObjectMap;
    private List<DescriptorGroupWithFeatures> descriptorGroupsWithFeatures;
    private DescriptorGroupSelectionDialog descriptorGroupSelectionDialog;

    public AddDescriptorGroupToTemplateAction(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
        this.descriptorGroups = new ArrayList<DescriptorGroup>();
        descriptorGroupSelectionDialog = new DescriptorGroupSelectionDialog(page.getRightSectionOfPage().getShell());
        descriptorGroupSelectionDialog.setInitialPattern("?");
    }

    @Override
    public void run()
    {
        CategoryTemplate categoryTemplate = (CategoryTemplate) tableViewer.getInput();
        descriptorGroupSelectionDialog.setTitle("Add to the Template ");
        List<String> descriptorGroupLabels = UtilityDescriptorDescriptorGroup.getDescriptorGroupLabels(categoryTemplate);
        descriptorGroupSelectionDialog.setNotAllowedLabels(descriptorGroupLabels);
        this.fillDialogChoices();
        descriptorGroupSelectionDialog.open();
        if(descriptorGroupSelectionDialog.getReturnCode() == Window.OK)
        {
            String selectedDescriptorGroupLabel = (String) descriptorGroupSelectionDialog.getFirstResult();
            String descriptorGroupUri = this.descriptorLabelToObjectMap.get(selectedDescriptorGroupLabel);
            DescriptorGroup descriptorGroup= null;
            for(DescriptorGroupWithFeatures d : this.descriptorGroupsWithFeatures){
                if(d.getUri().equals(descriptorGroupUri)){
                    descriptorGroup = d.getDescriptorGroup();
                    break;
                }
            }
            if(descriptorGroupSelectionDialog.maxOccurrence != null)
                descriptorGroup.setMaxOccurrence(descriptorGroupSelectionDialog.maxOccurrence);
            if(descriptorGroupSelectionDialog.mandatory)
            {
                categoryTemplate.addMandatoryDescriptorGroup(descriptorGroup);
            }
            else
            {
                categoryTemplate.addOptionalDescriptorGroup(descriptorGroup);
            }
            this.addDescriptorGroupToCategoryTemplate(categoryTemplate.getTemplateURI(), categoryTemplate.getUri(), descriptorGroup.getUri(), 
                    descriptorGroupSelectionDialog.maxOccurrence, 
                    descriptorGroupSelectionDialog.mandatory);
            this.refreshModifed();
        }
    }

    private void fillDialogChoices()
    {
        descriptorLabelToObjectMap = new HashMap<String, String>();
        descriptorGroupsWithFeatures = ((DescriptorGroupManagementPage) super.editor.findPage("1"))
                .getAllDescriptorGroupWithFeatures();
        for(DescriptorGroupWithFeatures descriptorWithFeatures : descriptorGroupsWithFeatures)
        {
            this.descriptorGroups.add(descriptorWithFeatures.getDescriptorGroup());
            this.descriptorLabelToObjectMap.put(descriptorWithFeatures.getLabel(), descriptorWithFeatures.getDescriptorGroup().getUri());
        }
        descriptorGroupSelectionDialog.setAllDescriptorGroups(descriptorGroups);
    }

    private void addDescriptorGroupToCategoryTemplate(String templateURI,
            String categoryURI, String descriptorURI, Integer maxOcurrence, boolean mandatory)
    {
        this.ontologyManagerApi.addDescriptorOrGroupToCategoryTemplate(templateURI, categoryURI, descriptorURI, 
                maxOcurrence, mandatory);
    }
}
