/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractDeleteAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor.UnitListLabelProvider;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;

/**
 * 
 *
 */
public class DeleteUnitFromDescriptorAction extends AbstractDeleteAction
{

    private String descriptorUri = null;

    public DeleteUnitFromDescriptorAction(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
    }

    public void run()
    {
        selectionIndex = this.tableViewer.getTable().getSelectionIndex();
        if(selectionIndex != -1)
        {
            MeasurementUnit selectedUnit = (MeasurementUnit) tableViewer.getTable().getItem(selectionIndex).getData();
            List<MeasurementUnit> units = ((Descriptor) this.tableViewer.getInput()).getValidUnits();
            int index = -1;
            for(MeasurementUnit unit : units)
            {
                if(unit.getUri().equals(selectedUnit.getUri()))
                {
                    index = units.indexOf(unit);
                    break;
                }
            }
            if(index != -1)
            {
                units.remove(index);
                Descriptor descriptor = (Descriptor) this.tableViewer.getInput();
                descriptorUri = descriptor.getUri();
                this.removeTripleFromOntology(descriptorUri, "has_unit_of_measurement", selectedUnit.getUri());
                if(descriptor.getDefaultMeasurementUnit().equals(selectedUnit.getUri())
                        && !units.isEmpty())
                {
                    String nextDefaultUnitUri = units.iterator().next().getUri();
                    this.ontologyManagerApi.setDefaultUnitForDescriptor(descriptorUri, nextDefaultUnitUri);
                    ((UnitListLabelProvider) this.tableViewer.getLabelProvider()).setDefaultUnitURI(nextDefaultUnitUri);
                    descriptor.setDefaultMeasurementUnit(nextDefaultUnitUri);
                    this.refreshModifed();
                }
            }
        }
    }

}
