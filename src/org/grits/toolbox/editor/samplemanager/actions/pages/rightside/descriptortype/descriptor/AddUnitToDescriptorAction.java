/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.dialogs.addunit.AddMeasurementUnitDialog;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor.UnitListLabelProvider;
import org.grits.toolbox.entry.sample.config.Config;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;
import org.grits.toolbox.entry.sample.utilities.UtilityDescriptorDescriptorGroup;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * 
 *
 */
public class AddUnitToDescriptorAction extends AbstractAddAction
{
    private static final Logger logger = Logger.getLogger(AddUnitToDescriptorAction.class);
    private ArrayList<MeasurementUnit> allUnits;
    private HashMap<String, String> unitLabelToUriMap;
    private AddMeasurementUnitDialog unitSelectionDialog;

    public AddUnitToDescriptorAction(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
        this.init();
    }

    private void init()
    {
        List<String> unitURIs = new ArrayList<String>();
        allUnits = new ArrayList<MeasurementUnit>();
        if(!super.editor.managerMode)
        {
            List<MeasurementUnit> unitsInStdOntology  = this.ontologyManagerApi.getAllMeasurementUnits(this.standardOntology);
            allUnits = new ArrayList<MeasurementUnit>(unitsInStdOntology);
            unitURIs = UtilityDescriptorDescriptorGroup.getURIs(unitsInStdOntology);
        }
        List<MeasurementUnit> unitsInLocalOntology  = this.ontologyManagerApi.getAllMeasurementUnits(this.localOntology);
        unitLabelToUriMap = new HashMap<String, String>(); 
        for(MeasurementUnit localUnit : unitsInLocalOntology)
        {
            if(!unitURIs.contains(localUnit.getUri()))
                allUnits.add(localUnit);
        }
        for(MeasurementUnit unit : allUnits)
        {
            unitLabelToUriMap.put(unit.getLabel(), unit.getUri());
        }
        
        OntModel ontologyModel = null;
        try
        {
            URL resourceFileUrl = FileLocator.toFileURL(Config.ONTOLOGY_RESOURCE_URL);
            String ontologyFilePath = resourceFileUrl.getPath() + "units"+ File.separator + Config.MANAGEMENT_UO_ONTOLOGY;
            FileInputStream inputStream = new FileInputStream(new File(ontologyFilePath));
            ontologyModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);
            ontologyModel.read(inputStream, OntologyManager.baseURI);
        } catch (IOException e)
        {
            logger.error(e.getMessage(), e);
        }
        unitSelectionDialog = new AddMeasurementUnitDialog(this.page.getRightSectionOfPage().getShell(), ontologyModel);
    }

    public void run()
    {
        Descriptor descriptor = ((Descriptor) this.tableViewer.getInput());
        List<MeasurementUnit> validUnits = descriptor.getValidUnits();
        List<String> existingURIs = UtilityDescriptorDescriptorGroup.getURIs(validUnits);
        unitSelectionDialog.setExistingURIs(existingURIs);
        unitSelectionDialog.open();
        if(unitSelectionDialog.getReturnCode() == Window.OK)
        {
            String selectedUnitURI = unitSelectionDialog.getSelectedMeasurementUnit().getUri();
            String selectedUnitLabel = unitSelectionDialog.getSelectedMeasurementUnit().getLabel();
            unitLabelToUriMap.put(selectedUnitLabel, selectedUnitURI );
            List<String> selectedUnits = new ArrayList<String>();
            selectedUnits.add(selectedUnitLabel);
            Set<String> unitLabels = unitLabelToUriMap.keySet();
            for(String unitLabel : selectedUnits)
            {
                if(unitLabels.contains(unitLabel))
                {
                    MeasurementUnit unit = unitSelectionDialog.getSelectedMeasurementUnit();
                    validUnits.add(unit);
                    this.addUnitToDescriptorInOntology(descriptor.getUri(), unit);
                    if(validUnits.size() == 1)
                    {
                        this.ontologyManagerApi.setDefaultUnitForDescriptor(descriptor.getUri(), unit.getUri());
                        descriptor.setDefaultMeasurementUnit(selectedUnitURI);
                        ((UnitListLabelProvider) this.tableViewer.getLabelProvider()).setDefaultUnitURI(unit.getUri());
                    }
                    this.refreshModifed();
                }
            }
        }
    }

    private void addUnitToDescriptorInOntology(String descriptorURI, MeasurementUnit unit)
    {
        this.ontologyManagerApi.addUnitToDescriptor(descriptorURI, unit);
    }
}
