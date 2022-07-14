/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor.AddNamespaceToDescriptorAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor.AddUnitToDescriptorAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor.DeleteNamespaceFromDescriptorAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor.DeleteUnitFromDescriptorAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor.SetDefaultUnitForDescriptorAction;
import org.grits.toolbox.editor.samplemanager.config.Config;
import org.grits.toolbox.editor.samplemanager.input.DescriptorWithFeatures;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.general.GeneralizedTablesWithActions;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.RightSideDescriptorTypeSection;

/**
 * 
 *
 */
public class RightSideDescriptorSection extends RightSideDescriptorTypeSection
{
    private Logger logger = Logger.getLogger(RightSideDescriptorSection.class);

    private Section namespaceListSection = null;
    private TableViewer namespaceTableViewer = null;
    private TableViewer unitTableViewer = null;
    private Section unitListSection = null;
    private AddUnitToDescriptorAction unitTableAddAction = null;
    private SetDefaultUnitForDescriptorAction unitTableSetDefaultAction = null;
    private DeleteUnitFromDescriptorAction unitTableDeleteAction = null;
    private AddNamespaceToDescriptorAction namespaceTableAddAction = null;
    private DeleteNamespaceFromDescriptorAction namespaceTableDeleteAction = null;
    private UnitListLabelProvider unitListLabelProvider = null;

    public RightSideDescriptorSection(AbstractManagementPage page)
    {
        super(page);
    }

    @Override
    protected void createMoreSpecificPart()
    {
        logger.debug("- START : Creating remaining part in Descriptor section.");
        try
        {
//        TypedListener g = (TypedListener) labelText.getListeners(SWT.Modify)[0];
//        LabelModifyListener labelModifyListener = (LabelModifyListener) g.getEventListener();
            List<String> existingLabels = ((ManagementEditor) page.getEditor())
                    .getAllIndividualLabels(OntologyManager.DESCRIPTOR_CLASS_URI);
            labelModifyListener.setExistingLabels(existingLabels);
            Composite namespaceListComposite = toolkit.createComposite(page.getRightComposite(), SWT.RIGHT);

            TableWrapLayout namespaceCompositeLayout = new TableWrapLayout();
            namespaceCompositeLayout.numColumns = 1;
            namespaceCompositeLayout.makeColumnsEqualWidth = true;
            namespaceListComposite.setLayout(namespaceCompositeLayout);
            this.createTableForNamespaces(namespaceListComposite);

            GridData compositeLayoutData = new GridData();
            compositeLayoutData.horizontalSpan = 4;
            namespaceListComposite.setLayoutData(compositeLayoutData);

            Composite unitListComposite = toolkit.createComposite(page.getRightComposite(), SWT.RIGHT);

            TableWrapLayout unitCompositeLayout = new TableWrapLayout();
            unitCompositeLayout.numColumns = 1;
            unitCompositeLayout.makeColumnsEqualWidth = true;
            unitListComposite.setLayout(unitCompositeLayout);
            this.createTableForUnits(unitListComposite);

            GridData unitCompositeLayoutData = new GridData();
            unitCompositeLayoutData.horizontalSpan = 4;
            unitListComposite.setLayoutData(unitCompositeLayoutData);
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Creating remaining part in Descriptor section.");
    }

    private void createTableForUnits(Composite unitListComposite)
    {
        //        DescriptorWithFeatures descriptorWithFeatures = (DescriptorWithFeatures) this.selectedObject;
        unitListSection = toolkit.createSection(unitListComposite, Section.TITLE_BAR);

        unitListSection.setText("Measurement Units");
        unitListSection.setLayout(new TableWrapLayout());

        TableWrapData section1LayoutData = new TableWrapData();
        section1LayoutData.colspan = 1;
        section1LayoutData.grabVertical = true;
        TableWrapData sectionData = new TableWrapData();
        sectionData.colspan = 1;
        unitListSection.setLayoutData(sectionData);

        GeneralizedTablesWithActions unitTableWithActions = new GeneralizedTablesWithActions(page, unitListSection);
        unitTableViewer = unitTableWithActions.createTableViewer(
                Config.MANAGEMENT_DESCRIPTOR_UNIT_TABLE_WIDTH, Config.MANAGEMENT_DESCRIPTOR_UNIT_TABLE_HEIGHT);
        unitTableViewer.setContentProvider(new DescriptorUnitContentProvider());
        unitListLabelProvider = new UnitListLabelProvider();
        unitTableViewer.setLabelProvider(unitListLabelProvider);
        TableColumn nameColumn = new TableColumn(unitTableViewer.getTable(), SWT.LEFT);
        TableColumn uriColumn = new TableColumn(unitTableViewer.getTable(), SWT.LEFT);
        nameColumn.setText("Name");
        int nameColumnWidth = 220;
        nameColumn.setWidth(nameColumnWidth);
        uriColumn.setText("URI");
        uriColumn.setWidth(Config.MANAGEMENT_DESCRIPTOR_UNIT_TABLE_WIDTH - nameColumnWidth);
        unitTableViewer.getTable().setHeaderVisible(true);
        //        unitTableViewer.setInput(descriptorWithFeatures.getDescriptor());
        unitTableAddAction = new AddUnitToDescriptorAction(this.page, 
                unitTableViewer);
        unitTableDeleteAction = new DeleteUnitFromDescriptorAction(this.page, 
                unitTableViewer);
        unitTableSetDefaultAction = new SetDefaultUnitForDescriptorAction(this.page, unitTableViewer);
        ToolBarManager toolBarManager = unitTableWithActions.getToolBarManager();
        toolBarManager.add(unitTableAddAction);
        toolBarManager.add(unitTableSetDefaultAction);
        toolBarManager.add(unitTableDeleteAction);
        toolBarManager.update(true);
    }

    private void createTableForNamespaces(Composite namespaceListComposite)
    {
        //        DescriptorWithFeatures descriptorWithFeatures = (DescriptorWithFeatures) this.selectedObject;
        namespaceListSection = toolkit.createSection(namespaceListComposite, Section.TITLE_BAR);

        namespaceListSection.setText("Namespaces");
        namespaceListSection.setLayout(new TableWrapLayout());

        TableWrapData section1LayoutData = new TableWrapData();
        section1LayoutData.colspan = 1;
        section1LayoutData.grabVertical = true;
        TableWrapData sectionData = new TableWrapData();
        sectionData.colspan = 1;
        namespaceListSection.setLayoutData(sectionData);

        GeneralizedTablesWithActions namespaceTableWithActions = 
                new GeneralizedTablesWithActions(page, namespaceListSection);
        namespaceTableViewer = namespaceTableWithActions.createTableViewer(
                Config.MANAGEMENT_DESCRIPTOR_NAMESPACE_TABLE_WIDTH, 
                Config.MANAGEMENT_DESCRIPTOR_NAMESPACE_TABLE_HEIGHT);

        namespaceTableViewer.setContentProvider(new NamespaceContentProvider());
        namespaceTableViewer.setLabelProvider(new NamespaceTableLabelProvider());
        TableColumn nameColumn = new TableColumn(namespaceTableViewer.getTable(), SWT.LEFT);
        TableColumn uriColumn = new TableColumn(namespaceTableViewer.getTable(), SWT.LEFT);
        nameColumn.setText("Name");
        int nameColumnWidth = 180;
        nameColumn.setWidth(nameColumnWidth);
        uriColumn.setText("URI");
        uriColumn.setWidth(Config.MANAGEMENT_DESCRIPTOR_NAMESPACE_TABLE_WIDTH - nameColumnWidth);
        namespaceTableViewer.getTable().setHeaderVisible(true);
//      namespaceTableViewer.setInput(descriptorWithFeatures.getDescriptor());
        namespaceTableAddAction = new AddNamespaceToDescriptorAction(this.page, 
                namespaceTableViewer);
        namespaceTableDeleteAction = new DeleteNamespaceFromDescriptorAction(this.page, 
                namespaceTableViewer);
        ToolBarManager toolBarManager = namespaceTableWithActions.getToolBarManager();
        toolBarManager.add(namespaceTableAddAction);
        toolBarManager.add(namespaceTableDeleteAction);
        toolBarManager.update(true);
    }

    @Override
    protected void resetMoreSpecificPart()
    {
        logger.debug("- START : Resetting remaining part in Descriptor section for the selection.");
        try
        {
            DescriptorWithFeatures descriptorWithFeatures = (DescriptorWithFeatures) this.selectedObject;
            List<String> existingLabels = ((ManagementEditor) page.getEditor())
                    .getAllIndividualLabels(OntologyManager.DESCRIPTOR_CLASS_URI);
            labelModifyListener.setExistingLabels(existingLabels);
            namespaceTableViewer.setInput(descriptorWithFeatures.getDescriptor());
            namespaceTableAddAction.setEnabled(editable);
            namespaceTableDeleteAction.setEnabled(editable);

            if(descriptorWithFeatures.getDescriptor().getDefaultMeasurementUnit() != null)
                unitListLabelProvider.setDefaultUnitURI(descriptorWithFeatures.getDescriptor().getDefaultMeasurementUnit());
            unitTableViewer.setInput(descriptorWithFeatures.getDescriptor());
            unitTableAddAction.setEnabled(editable);
            unitTableSetDefaultAction.setEnabled(editable);
            unitTableDeleteAction.setEnabled(editable);
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Resetting remaining part in Descriptor section for the selection.");

    }

}
