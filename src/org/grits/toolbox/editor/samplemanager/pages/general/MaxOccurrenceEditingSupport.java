/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.general;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.core.utilShare.validator.NumericValidator;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.ontology.GritsOntologyManagerApi;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;
import org.grits.toolbox.entry.sample.utilities.CellEditorWithSpinner;

/**
 * 
 *
 */
public class MaxOccurrenceEditingSupport extends EditingSupport
{
    private AbstractManagementPage page = null;
    private TableViewer tableViewer = null;
    private GritsOntologyManagerApi ontologyManager = null;
    private CellEditorWithSpinner spinnerCellEditor = null;
    private boolean editable = true;

    public MaxOccurrenceEditingSupport(AbstractManagementPage page, TableViewer tableViewer)
    {
        super(tableViewer);
        this.page = page;
        this.tableViewer = tableViewer;
        ontologyManager = ((ManagementEditor) this.page.getEditor()).getOntologyManagerApi();
        this.spinnerCellEditor = new CellEditorWithSpinner(tableViewer.getTable());
        spinnerCellEditor.setValidator(new NumericValidator());
    }

    @Override
    protected CellEditor getCellEditor(Object element)
    {
        Integer value = null;
        if(element instanceof Descriptor)
        {
            value = ((Descriptor) element).getMaxOccurrence();
        }
        else if(element instanceof DescriptorGroup)
        {
            value = ((DescriptorGroup) element).getMaxOccurrence();
        }
        spinnerCellEditor.setValue(value);
        return spinnerCellEditor;
    }

    @Override
    protected boolean canEdit(Object element)
    {
        Object input = tableViewer.getInput();
        if(input instanceof DescriptorGroup 
                || input instanceof CategoryTemplate)
            return editable;
        else
            return false;
    }

    @Override
    protected Object getValue(Object element)
    {
        if(element instanceof Descriptor)
        {
                return ((Descriptor) element).getMaxOccurrence();
        }
        else if(element instanceof DescriptorGroup)
        {
                return ((DescriptorGroup) element).getMaxOccurrence();
        }
        return null;
    }

    @Override
    protected void setValue(Object element, Object setValue)
    {
        Integer intValue = ((Integer) setValue);
        if(element instanceof Descriptor)
        {
            Descriptor descriptor = ((Descriptor) element);
            descriptor.setMaxOccurrence(intValue);
            if(tableViewer.getInput() instanceof DescriptorGroup)
            {
                DescriptorGroup dg = (DescriptorGroup) tableViewer.getInput();
                this.ontologyManager.setMaxOccurrenceInGroup(dg.getUri(), descriptor.getUri(), intValue);
                this.refreshModified();
            }
            else if(tableViewer.getInput() instanceof CategoryTemplate)
            {
                CategoryTemplate categoryTemplate = (CategoryTemplate) tableViewer.getInput();
                this.ontologyManager.setMaxOccurrenceInTemplate(categoryTemplate.getTemplateURI(), 
                        categoryTemplate.getUri(), descriptor.getUri(), intValue);
                this.refreshModified();
            }
        }
        else if(element instanceof DescriptorGroup)
        {
            DescriptorGroup descriptorGroup = ((DescriptorGroup) element);
            descriptorGroup.setMaxOccurrence(intValue);
            if(tableViewer.getInput() instanceof CategoryTemplate)
            {
                CategoryTemplate categoryTemplate = (CategoryTemplate) tableViewer.getInput();
                this.ontologyManager.setMaxOccurrenceInTemplate(categoryTemplate.getTemplateURI(), 
                        categoryTemplate.getUri(), descriptorGroup.getUri(), intValue);
                this.refreshModified();
            }
        }
    }

    private void refreshModified()
    {
        this.tableViewer.refresh();
        ((ManagementEditor) this.page.getEditor()).markDirty();
    }

}
