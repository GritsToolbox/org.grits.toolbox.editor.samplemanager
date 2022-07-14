/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.dialogs.DescriptorSelectionDialog;
import org.grits.toolbox.editor.samplemanager.input.DescriptorWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.DesciptorManagementPage;
import org.grits.toolbox.entry.sample.model.Category;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.utilities.UtilityDescriptorDescriptorGroup;

/**
 * 
 *
 */
public class AddDescriptorToCategoryAction extends AbstractAddAction
{
    private ArrayList<Descriptor> descriptors;
    private HashMap<String, String> descriptorLabelToObjectMap;
    private List<DescriptorWithFeatures> descriptorsWithFeatures;
    private DescriptorSelectionDialog descriptorSelectionDialog;

    public AddDescriptorToCategoryAction(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
        this.descriptors = new ArrayList<Descriptor>();
        descriptorSelectionDialog = new DescriptorSelectionDialog(page.getRightSectionOfPage().getShell(), false);
        descriptorSelectionDialog.setInitialPattern("?");
    }

    @Override
    public void run()
    {
        Category selectedCategory = (Category) tableViewer.getInput();
        descriptorSelectionDialog.setTitle("Add to the " + super.editor.getAllCategoriesURILabelMap().get(selectedCategory.getUri()));
        List<String> descriptorLabels = UtilityDescriptorDescriptorGroup.getDescriptorLabels(selectedCategory);
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
            
            selectedCategory.addDescriptor(descriptor);
            
            this.ontologyManagerApi.updateCategorySelection(selectedCategory.getUri(), descriptor.getUri(), true);
            this.ontologyManagerApi.updateMaxOccurrence(descriptor.getUri(), descriptorSelectionDialog.maxOccurrence);
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

}
