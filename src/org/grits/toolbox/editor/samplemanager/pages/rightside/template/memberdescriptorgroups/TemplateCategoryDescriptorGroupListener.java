/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.forms.widgets.Section;
import org.grits.toolbox.editor.samplemanager.input.TemplateWithFeatures;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.entry.sample.model.Category;

/**
 * 
 *
 */
public class TemplateCategoryDescriptorGroupListener implements
ISelectionChangedListener
{

    private CheckboxTableViewer tableViewer;
    private TemplateWithFeatures selectedTemplate;

    public TemplateCategoryDescriptorGroupListener(CheckboxTableViewer tableViewer)
    {
        this.tableViewer = tableViewer;
        this.setSelectedTemplate(selectedTemplate);

    }

    public void setSelectedTemplate(TemplateWithFeatures selectedTemplate)
    {
        this.selectedTemplate = selectedTemplate;
        tableViewer.refresh();
    }


    @Override
    public void selectionChanged(SelectionChangedEvent event)
    {
        ComboViewer cViewer = ((ComboViewer) event.getSource());
        Category selectedCategory = (Category) cViewer.getElementAt(cViewer.getCombo().getSelectionIndex());
        this.setCategoryTemplate(selectedCategory, tableViewer);
        tableViewer.refresh();
        //        List<DescriptorGroup> descriptors = this.getCategoryDescriptorGroups(selectedCategory);
        //        List<String> mandUris = this.getCategoryMandatoryDescriptorURIs(selectedCategory);
        //        leftSideTableViewer.setInput(descriptors);
        //        for(DescriptorGroup desc : descriptors)
        //        {
        //            if(mandUris.contains(desc.getUri()))
        //                leftSideTableViewer.setChecked(desc, true);
        //        }
        //        leftSideTableViewer.getTable().setEnabled(selectedTemplate.isEditable());
        //        leftSideTableViewer.refresh();
    }

    private void setCategoryTemplate(Category selectedCategory,
            CheckboxTableViewer tableViewer)
    {
        Section tableViewerSection = (Section) tableViewer.getTable().getParent().getParent();
        tableViewerSection.getTextClient().setEnabled(true);
        switch(selectedCategory.getUri())
        {
        case OntologyManager.CATEGORY_AMOUNT_CLASS_URI :
            tableViewer.setInput(selectedTemplate.getTemplate().getAmountTemplate());
            break;
        case OntologyManager.CATEGORY_PURITY_QC_CLASS_URI :
            tableViewer.setInput(selectedTemplate.getTemplate().getPurityQCTemplate());
            break;
        case OntologyManager.CATEGORY_SAMPLE_INFO_CLASS_URI :
            tableViewer.setInput(selectedTemplate.getTemplate().getSampleInformationTemplate());
            break;
        case OntologyManager.CATEGORY_TRACKING_INFO_CLASS_URI :
            tableViewer.setInput(selectedTemplate.getTemplate().getTrackingTemplate());
            break;
        default :
//            ArrayList<CategoryTemplate> templates = new ArrayList<CategoryTemplate>();
//            templates.add(selectedTemplate.getTemplate().getAmountTemplate());
//            templates.add(selectedTemplate.getTemplate().getPurityQCTemplate());
//            templates.add(selectedTemplate.getTemplate().getSampleInformationTemplate());
//            templates.add(selectedTemplate.getTemplate().getTrackingTemplate());
//            tableViewer.setInput(templates);
            tableViewerSection.getTextClient().setEnabled(false);
        }
        return;

    }

}
