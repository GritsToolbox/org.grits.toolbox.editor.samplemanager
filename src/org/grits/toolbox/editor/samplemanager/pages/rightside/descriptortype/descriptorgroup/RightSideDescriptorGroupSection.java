/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptorgroup.AddDescriptorToGroupAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptorgroup.DeleteDescriptorFromGroupAction;
import org.grits.toolbox.editor.samplemanager.input.DescriptorGroupWithFeatures;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.general.CheckboxTableUtility;
import org.grits.toolbox.editor.samplemanager.pages.general.SectionUtility;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.RightSideDescriptorTypeSection;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup.tabledescriptorlist.DescriptorListLabelProvider;
import org.grits.toolbox.editor.samplemanager.util.GenericColumnsWithCheckboxSelectionListener;
import org.grits.toolbox.editor.samplemanager.util.GenericViewerComparatorCheckboxTable;

/**
 * 
 *
 */
public class RightSideDescriptorGroupSection extends RightSideDescriptorTypeSection
{
    private Logger logger = Logger.getLogger(RightSideDescriptorGroupSection.class);

    private Section descriptorListSection = null;
    private CheckboxTableViewer tableViewer = null;
    //  private List<Descriptor> descriptors;
    private AddDescriptorToGroupAction addDescriptorToGroupAction = null;
    private DeleteDescriptorFromGroupAction deleteDescriptorFromGroupAction = null;

    public RightSideDescriptorGroupSection(AbstractManagementPage page)
    {
        super(page);
    }

    @Override
    protected void createMoreSpecificPart()
    {
        logger.debug("- START : Creating remaining part in Descriptor Group section.");
        try
        {
            List<String> existingLabels = ((ManagementEditor) page.getEditor())
                    .getAllIndividualLabels(OntologyManager.DESCRIPTOR_GROUP_CLASS_URI);
            labelModifyListener.setExistingLabels(existingLabels);
            this.createTableForDescriptors();
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Creating remaining part in Descriptor Group section.");
    }

    private void createTableForDescriptors()
    {
//      DescriptorGroupWithFeatures descriptorGroupWithFeatures = (DescriptorGroupWithFeatures) this.selectedObject;
        descriptorListSection = SectionUtility.createSectionForCategory(toolkit, page.getRightComposite(), 
                "Descriptors");
        Composite descriptorListSectionComposite = SectionUtility.getCompositeInsideSection(
                toolkit, descriptorListSection, 1, 150, 10, 2);
        tableViewer = CheckboxTableUtility.createCheckboxTableViewer(page, descriptorListSectionComposite, SWT.CHECK, "Descriptors");
        tableViewer.setContentProvider(new DescriptorGroupMembersContenProvider());
        tableViewer.setLabelProvider(new DescriptorListLabelProvider());
        tableViewer.setCheckStateProvider(new MandatoryCheckDescriptorProvider(tableViewer));
        tableViewer.addCheckStateListener(new CheckMandatoryDescriptorInGroupListener(page, tableViewer));
//        tableViewer.setComparator(new TextComparatorTableColumn1());

        GenericViewerComparatorCheckboxTable simpleTypeViewerComparator = new GenericViewerComparatorCheckboxTable();
        tableViewer.setComparator(simpleTypeViewerComparator);
        this.addColumnSelectionListenerForCheckbox(tableViewer);

        TableWrapData tData = new TableWrapData();
        tData.heightHint = 200;
        tableViewer.getTable().setLayoutData(tData);
        ToolBarManager toolBarManager = new ToolBarManager(SWT.BALLOON);
        addDescriptorToGroupAction = this.getAddDescriptorToGroupAction();
        toolBarManager.add(addDescriptorToGroupAction);
        deleteDescriptorFromGroupAction = this.getDeleteDescriptorFromGroupAction();
        toolBarManager.add(deleteDescriptorFromGroupAction);
        ToolBar toolbar = toolBarManager.createControl(descriptorListSection);
        descriptorListSection.setTextClient(toolbar);
//      descriptorListSection.setExpanded(false);
//      descriptorListSection.setExpanded(true);
    }


    private void addColumnSelectionListenerForCheckbox(CheckboxTableViewer checkboxTableViewer)
    {
        int totalColumns = checkboxTableViewer.getTable().getColumns().length;
        for(int i = 0 ; i < totalColumns ; i++)
        {
            checkboxTableViewer.getTable().getColumn(i).addSelectionListener(
                    new GenericColumnsWithCheckboxSelectionListener(checkboxTableViewer));
        }
    }

    private DeleteDescriptorFromGroupAction getDeleteDescriptorFromGroupAction()
    {
        return new DeleteDescriptorFromGroupAction(this.page, tableViewer);
    }

    private AddDescriptorToGroupAction getAddDescriptorToGroupAction()
    {
        return new AddDescriptorToGroupAction(this.page, tableViewer);
    }

    @Override
    protected void resetMoreSpecificPart()
    {
        logger.debug("- START : Resetting remaining part in Descriptor Group section for the selection.");
        try
        {
            descriptorListSection.setExpanded(false);

            List<String> existingLabels = ((ManagementEditor) page.getEditor())
                    .getAllIndividualLabels(OntologyManager.DESCRIPTOR_GROUP_CLASS_URI);
            labelModifyListener.setExistingLabels(existingLabels);

            DescriptorGroupWithFeatures descriptorGroupWithFeatures = ((DescriptorGroupWithFeatures) this.selectedObject);
            tableViewer.setInput(descriptorGroupWithFeatures.getDescriptorGroup());

            addDescriptorToGroupAction.setEnabled(editable);
            deleteDescriptorFromGroupAction.setEnabled(editable);
            tableViewer.getTable().setEnabled(editable);
            descriptorListSection.setExpanded(true);
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Resetting remaining part in Descriptor Group section for the selection.");
    }

}
