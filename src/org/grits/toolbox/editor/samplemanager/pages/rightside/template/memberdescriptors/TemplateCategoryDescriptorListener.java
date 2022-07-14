/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors;

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
public class TemplateCategoryDescriptorListener implements ISelectionChangedListener
{

    private CheckboxTableViewer tableViewer;
    private TemplateWithFeatures selectedTemplate;

    public TemplateCategoryDescriptorListener(CheckboxTableViewer tableViewer)
    {
        this.tableViewer = tableViewer;
    }

    public void setSelectedTemplate(TemplateWithFeatures selectedTemplate)
    {
        this.selectedTemplate = selectedTemplate;
        tableViewer.refresh();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event)
    {
        ComboViewer cViewer = ((ComboViewer) event.getSource());
        Category selectedDescriptorCategory = (Category) cViewer.getElementAt(cViewer.getCombo().getSelectionIndex());
        this.setCategoryTemplate(selectedDescriptorCategory, tableViewer);
    }

    private void setCategoryTemplate(Category selectedDescriptorCategory, CheckboxTableViewer tableViewer)
    {
        Section tableViewerSection = (Section) tableViewer.getTable().getParent().getParent();
        tableViewerSection.getTextClient().setEnabled(true);
        switch(selectedDescriptorCategory.getUri())
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
