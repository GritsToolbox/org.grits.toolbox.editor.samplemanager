/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.ontology.GritsOntologyManagerApi;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor.UnitListLabelProvider;
import org.grits.toolbox.entry.sample.config.ImageRegistry;
import org.grits.toolbox.entry.sample.config.ImageRegistry.SampleImage;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;

/**
 * 
 *
 */
public class SetDefaultUnitForDescriptorAction extends Action
{
    private static final Logger logger = Logger.getLogger(SetDefaultUnitForDescriptorAction.class);
    protected AbstractManagementPage page = null;
    protected TableViewer tableViewer = null;
    protected GritsOntologyManagerApi ontologyManagerApi = null;
    protected ManagementEditor editor;

    public SetDefaultUnitForDescriptorAction(AbstractManagementPage page, TableViewer tableViewer) {
        this.page  = page;
        this.tableViewer  = tableViewer;
        initDataSource();
        this.setupUI();
    }

    protected void initDataSource()
    {
        editor = ((ManagementEditor) this.page.getEditor());
        this.ontologyManagerApi  = editor.getOntologyManagerApi();
    }

    protected void setupUI() {
        this.setToolTipText("Set as default");
        ImageDescriptor imageDesc = ImageRegistry.getImageDescriptor(SampleImage.CHECKBOX_TICKED_ICON);
        Image image = imageDesc.createImage();
        this.setImageDescriptor(ImageDescriptor.createFromImage(image));
    }

    public void run()
    {
        logger.debug("- START : Setting default measurement unit for descriptor");

        int selectionIndex = this.tableViewer.getTable().getSelectionIndex();
        if(selectionIndex != -1)
        {
            Descriptor descriptor = ((Descriptor) this.tableViewer.getInput());
            MeasurementUnit selectedUnit = (MeasurementUnit) tableViewer.getTable().getItem(selectionIndex).getData();
            this.ontologyManagerApi.setDefaultUnitForDescriptor(descriptor.getUri(), selectedUnit.getUri());
            descriptor.setDefaultMeasurementUnit(selectedUnit.getUri());
            ((UnitListLabelProvider) this.tableViewer.getLabelProvider()).setDefaultUnitURI(selectedUnit.getUri());
            this.refreshModifed();
        }

        logger.debug("- END   : Setting default measurement unit for descriptor");
    }

    public void refreshModifed()
    {
        this.tableViewer.refresh();
        ((ManagementEditor) this.page.getEditor()).markDirty();
    }

}
