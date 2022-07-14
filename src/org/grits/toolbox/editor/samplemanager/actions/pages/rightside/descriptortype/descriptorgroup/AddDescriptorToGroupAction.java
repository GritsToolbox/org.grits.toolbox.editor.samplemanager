/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptorgroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.window.Window;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.dialogs.DescriptorSelectionDialog;
import org.grits.toolbox.editor.samplemanager.input.DescriptorWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.DesciptorManagementPage;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;
import org.grits.toolbox.entry.sample.utilities.UtilityDescriptorDescriptorGroup;

/**
 * 
 *
 */
public class AddDescriptorToGroupAction extends AbstractAddAction
{
    private CheckboxTableViewer tableViewer = null;
    private ArrayList<Descriptor> descriptors;
    private DescriptorSelectionDialog descriptorSelectionDialog;
    private HashMap<String, String> descriptorLabelToObjectMap;
    private List<DescriptorWithFeatures> descriptorsWithFeatures;

    public AddDescriptorToGroupAction(AbstractManagementPage page, 
            CheckboxTableViewer tableViewer)
    {
        super(page, tableViewer);
        this.tableViewer = tableViewer;
        this.descriptors = new ArrayList<Descriptor>();
        descriptorSelectionDialog = new DescriptorSelectionDialog(page.getRightSectionOfPage().getShell());
        descriptorSelectionDialog.setInitialPattern("?");
    }
    
    public void run()
    {
        DescriptorGroup descriptorGroup = (DescriptorGroup) tableViewer.getInput();
        descriptorSelectionDialog.setTitle("Add to the Descriptor Group : " + descriptorGroup.getLabel());
        List<String> descriptorLabels = UtilityDescriptorDescriptorGroup.getDescriptorLabels(descriptorGroup);
        descriptorSelectionDialog.setNotAllowedLabels(descriptorLabels);
        this.fillDialogChoices();
        descriptorSelectionDialog.open();
        if(descriptorSelectionDialog.getReturnCode() == Window.OK)
        {
            String selectedDescriptorLabel = (String) descriptorSelectionDialog.getFirstResult();
            String descriptorUri = this.descriptorLabelToObjectMap.get(selectedDescriptorLabel);
            Descriptor descriptor = null;
            for(DescriptorWithFeatures d : this.descriptorsWithFeatures){
                if(d.getUri().equals(descriptorUri)){
                    descriptor = d.getDescriptor();
                    break;
                }
            }
            if(descriptorSelectionDialog.maxOccurrence != null)
                descriptor.setMaxOccurrence(descriptorSelectionDialog.maxOccurrence);
            if(descriptorSelectionDialog.mandatory)
            {
                descriptorGroup.addMandatoryDescriptor(descriptor);
//                leftSideTableViewer.setChecked(descriptor, true);
            }
            else
            {
                descriptorGroup.addOptionalDescriptor(descriptor);
//                leftSideTableViewer.setChecked(descriptor, false);
            }
            this.addDescriptorToGroupInOntology(descriptorGroup.getUri(), descriptor.getUri(), 
                    descriptorSelectionDialog.maxOccurrence, 
                    descriptorSelectionDialog.mandatory);
            this.refreshModifed();
        }
    }

    private void fillDialogChoices()
    {
        descriptorLabelToObjectMap = new HashMap<String, String>();
        descriptorsWithFeatures = ((DesciptorManagementPage) super.editor.findPage("0"))
                .getAllDescriptorWithFeatures();
        for(DescriptorWithFeatures descriptorWithFeatures : descriptorsWithFeatures)
        {
            this.descriptors.add(descriptorWithFeatures.getDescriptor());
            this.descriptorLabelToObjectMap.put(descriptorWithFeatures.getLabel(), descriptorWithFeatures.getDescriptor().getUri());
        }
        descriptorSelectionDialog.setAllDescriptors(descriptors);
    }

    private void addDescriptorToGroupInOntology(String groupURI, String descriptorURI, Integer maxOcurrence,
            boolean mandatory)
    {
        this.ontologyManagerApi.addDescriptorToGroup(groupURI, descriptorURI, 
                maxOcurrence, mandatory);
    }

}
