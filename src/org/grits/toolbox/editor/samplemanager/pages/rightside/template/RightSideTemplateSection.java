/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.template.AddDescriptorGroupToTemplateAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.template.AddDescriptorToTemplateAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.template.DeleteDescriptorFromTemplateAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.template.DeleteDescriptorGroupFromTemplateAction;
import org.grits.toolbox.editor.samplemanager.input.TemplateWithFeatures;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.CategoryManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.general.CheckboxTableUtility;
import org.grits.toolbox.editor.samplemanager.pages.general.SectionUtility;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSection;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups.CheckListenerMandatoryGroupInTemplate;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups.DescriptorGroupLabelProvider;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups.MandatoryCheckTemplateDescriptorGroupProvider;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups.TemplateCategoryDescriptorGroupListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptorgroups.TemplateDescriptorGroupMembersContentProvider;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors.CheckMandatoryDescriptorInCategoryTemplateListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors.DescriptorLabelProvider;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors.MandatoryCheckTemplateDescriptorProvider;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors.TemplateCategoryDescriptorListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.memberdescriptors.TemplateDescriptorMembersContentProvider;
import org.grits.toolbox.editor.samplemanager.util.GenericColumnsWithCheckboxSelectionListener;
import org.grits.toolbox.editor.samplemanager.util.GenericViewerComparatorCheckboxTable;
import org.grits.toolbox.entry.sample.model.Category;

/**
 * 
 *
 */
public class RightSideTemplateSection extends RightSideSection
{
    private Logger logger = Logger.getLogger(RightSideTemplateSection.class);

    private Composite descriptorListSectionComposite = null;
    private CheckboxTableViewer tableViewer;
    private AddDescriptorToTemplateAction addDescriptorToTemplateAction;

    private DeleteDescriptorFromTemplateAction deleteDescriptorFromTemplateAction;
    private ComboViewer categoryComboForDescriptors = null;
    private ComboViewer categoryCombo = null;
    private Composite descriptorGroupListSectionComposite;
    private CheckboxTableViewer dgTableViewer;
    private AddDescriptorGroupToTemplateAction addDescriptorGroupToTemplateAction;
    private DeleteDescriptorGroupFromTemplateAction deleteDescriptorGroupFromTemplateAction;
    private HashMap<String, String> uriLabelMap;
    private List<Category> categories;
    private TemplateCategoryDescriptorListener categoryChangedForDescriptorsListener;
    private TemplateCategoryDescriptorGroupListener categoryChangedForDescriptorGroupsListener;

    public RightSideTemplateSection(AbstractManagementPage page)
    {
        super(page);
        uriLabelMap = ((ManagementEditor) page.getEditor()).getAllCategoriesURILabelMap();
    }

    @Override
    protected void createSpecificPart()
    {
        logger.debug("- START : Creating specific part in right side Template section.");
        try
        {
            List<String> existingLabels = ((ManagementEditor) page.getEditor())
                    .getAllIndividualLabels(OntologyManager.TEMPLATE_CLASS_URI);
            labelModifyListener.setExistingLabels(existingLabels);
            categories = new ArrayList<Category>(((CategoryManagementPage) 
                    ((ManagementEditor) page.getEditor()).findPage("3"))
                    .getAllCategories());
//            Category showAllCategory = new Category("Show All");
//            categories.add(showAllCategory);
            this.createDescriptorTableSection();
            this.createDescriptorGroupTableSection();
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Creating specific part in right side Template section.");
    }

    private void createDescriptorTableSection()
    {
        //        TemplateWithFeatures selectedTemplateObject = (TemplateWithFeatures) this.selectedObject;
        Section descriptorListSection = SectionUtility.createSectionForCategory(toolkit, 
                page.getRightComposite(), "Descriptors");

        descriptorListSectionComposite = SectionUtility.getCompositeInsideSection(toolkit, 
                descriptorListSection, 1, 10, 2, 2);

        Composite categoryComboComposite = toolkit.createComposite(descriptorListSectionComposite);
        TableWrapLayout comboCompositeLayout = new TableWrapLayout();
        comboCompositeLayout.numColumns = 2;
        comboCompositeLayout.horizontalSpacing = 30;
        categoryComboComposite.setLayout(comboCompositeLayout);
        Label categoryLabel = toolkit.createLabel(categoryComboComposite , "Category");
        categoryLabel.setFont(boldFont);
        TableWrapData labelData = new TableWrapData();
        categoryLabel.setLayoutData(labelData);

        this.categoryComboForDescriptors =  new ComboViewer(categoryComboComposite, SWT.READ_ONLY);
        TableWrapData comboData = new TableWrapData(TableWrapData.LEFT);
        categoryComboForDescriptors.getCombo().setLayoutData(comboData);
        this.setUpContentForCombo(categoryComboForDescriptors);
        TableWrapData comboCompositeData = new TableWrapData();
        comboCompositeData.colspan = 1;
        categoryComboComposite.setLayoutData(comboCompositeData);

        tableViewer = CheckboxTableUtility.createCheckboxTableViewer(page,
        		descriptorListSectionComposite, SWT.CHECK, "Descriptors");
        tableViewer.setContentProvider(new TemplateDescriptorMembersContentProvider());
        tableViewer.setLabelProvider(new DescriptorLabelProvider());
        tableViewer.setCheckStateProvider(new MandatoryCheckTemplateDescriptorProvider(tableViewer));
        tableViewer.addCheckStateListener(new CheckMandatoryDescriptorInCategoryTemplateListener(page, tableViewer));
//        tableViewer.setComparator(new TextComparatorTableColumn1());

        GenericViewerComparatorCheckboxTable simpleTypeViewerComparator = new GenericViewerComparatorCheckboxTable();
        tableViewer.setComparator(simpleTypeViewerComparator);
        this.addColumnSelectionListenerForCheckbox(tableViewer);

        TableWrapData tableData = new TableWrapData(TableWrapData.FILL_GRAB);
        tableData.colspan = 1;
        tableData.heightHint = 150;
        tableViewer.getTable().setLayoutData(tableData);
        ToolBarManager toolBarManager = new ToolBarManager(SWT.BALLOON);
        addDescriptorToTemplateAction = this.getAddDescriptorToTemplateAction();
        toolBarManager.add(addDescriptorToTemplateAction);
        deleteDescriptorFromTemplateAction = this.getDeleteDescriptorFromTemplateAction();
        toolBarManager.add(deleteDescriptorFromTemplateAction);
        ToolBar toolbar = toolBarManager.createControl(descriptorListSection);
        descriptorListSection.setTextClient(toolbar);
        categoryChangedForDescriptorsListener = new TemplateCategoryDescriptorListener(tableViewer);
        categoryComboForDescriptors.addSelectionChangedListener(categoryChangedForDescriptorsListener);
        categoryComboForDescriptors.getCombo().select(categories.size()-1);
    }

    private void addColumnSelectionListenerForCheckbox(
            CheckboxTableViewer checkboxTableViewer)
    {
        int totalColumns = checkboxTableViewer.getTable().getColumns().length;
        for(int i = 0 ; i < totalColumns ; i++)
        {
            checkboxTableViewer.getTable().getColumn(i).addSelectionListener(
                    new GenericColumnsWithCheckboxSelectionListener(checkboxTableViewer));
        }
    }

    private void setUpContentForCombo(ComboViewer categoryComboForDescriptors)
    {
        categoryComboForDescriptors.setContentProvider(new CategoryComboContentProvider());
        categoryComboForDescriptors.setLabelProvider(new CategoryComboLabelProvider(uriLabelMap));
        categoryComboForDescriptors.setInput(categories);
    }

    private void createDescriptorGroupTableSection()
    {
        //        TemplateWithFeatures selectedTemplateObject = (TemplateWithFeatures) this.selectedObject;

        Section descriptorGroupListSection = SectionUtility.createSectionForCategory(toolkit, 
                page.getRightComposite(), "Descriptor Groups");

        descriptorGroupListSectionComposite  = SectionUtility.getCompositeInsideSection(toolkit, 
                descriptorGroupListSection, 1, 10, 2, 2);

        Composite categoryComboComposite = toolkit.createComposite(descriptorGroupListSectionComposite);
        TableWrapLayout comboCompositeLayout = new TableWrapLayout();
        comboCompositeLayout.numColumns = 2;
        comboCompositeLayout.horizontalSpacing = 30;
        categoryComboComposite.setLayout(comboCompositeLayout);

        Label categoryLabel = toolkit.createLabel(categoryComboComposite, "Category");
        categoryLabel.setFont(boldFont);
        TableWrapData labelData = new TableWrapData(TableWrapData.RIGHT);
        categoryLabel.setLayoutData(labelData);

        this.categoryCombo =  new ComboViewer(categoryComboComposite, SWT.READ_ONLY);
        TableWrapData comboData = new TableWrapData(TableWrapData.LEFT);
        categoryCombo.getCombo().setLayoutData(comboData);

        this.setUpContentForCombo(categoryCombo);
        categoryCombo.getCombo().select(categories.size()-1);
        TableWrapData comboCompositeData = new TableWrapData();
        comboCompositeData.colspan = 1;
        categoryComboComposite.setLayoutData(comboCompositeData);

        dgTableViewer = CheckboxTableUtility.createCheckboxTableViewer(page,
        		descriptorGroupListSectionComposite, SWT.CHECK, "Descriptor Groups");
        dgTableViewer.setContentProvider(new TemplateDescriptorGroupMembersContentProvider());
        dgTableViewer.setLabelProvider(new DescriptorGroupLabelProvider());
        dgTableViewer.setCheckStateProvider(new MandatoryCheckTemplateDescriptorGroupProvider(dgTableViewer));
        dgTableViewer.addCheckStateListener(new CheckListenerMandatoryGroupInTemplate(page, dgTableViewer));

        dgTableViewer.setComparator(new GenericViewerComparatorCheckboxTable());
        this.addColumnSelectionListenerForCheckbox(dgTableViewer);

        TableWrapData tableData = new TableWrapData(TableWrapData.FILL_GRAB);
        tableData.colspan = 1;
        tableData.heightHint = 150;
        dgTableViewer.getTable().setLayoutData(tableData);
        ToolBarManager toolBarManager = new ToolBarManager();
        addDescriptorGroupToTemplateAction = this.getAddDescriptorGroupToTemplateAction();
        toolBarManager.add(addDescriptorGroupToTemplateAction);
        deleteDescriptorGroupFromTemplateAction = this.getDeleteDescriptorGroupFromTemplateAction();
        toolBarManager.add(deleteDescriptorGroupFromTemplateAction);
        ToolBar toolbar = toolBarManager.createControl(descriptorGroupListSection);
        descriptorGroupListSection.setTextClient(toolbar);

        categoryChangedForDescriptorGroupsListener = new TemplateCategoryDescriptorGroupListener(dgTableViewer);
        categoryCombo.addSelectionChangedListener(categoryChangedForDescriptorGroupsListener);
        categoryCombo.getCombo().select(categories.size()-1);
    }

    private DeleteDescriptorGroupFromTemplateAction getDeleteDescriptorGroupFromTemplateAction()
    {
        return new DeleteDescriptorGroupFromTemplateAction(page, dgTableViewer);
    }

    private AddDescriptorGroupToTemplateAction getAddDescriptorGroupToTemplateAction()
    {
        return new AddDescriptorGroupToTemplateAction(page, dgTableViewer);
    }

    private DeleteDescriptorFromTemplateAction getDeleteDescriptorFromTemplateAction()
    {
        return new DeleteDescriptorFromTemplateAction(page, tableViewer);
    }

    private AddDescriptorToTemplateAction getAddDescriptorToTemplateAction()
    {
        return new AddDescriptorToTemplateAction(page, tableViewer);
    }

    protected void resetSpecificPart()
    {
        logger.debug("- START : Resetting right side Template section for the selection.");
        try
        {
            TemplateWithFeatures selectedTemplate = (TemplateWithFeatures) this.selectedObject;

            categoryChangedForDescriptorsListener.setSelectedTemplate(selectedTemplate);
            List<String> existingLabels = ((ManagementEditor) page.getEditor())
                    .getAllIndividualLabels(OntologyManager.TEMPLATE_CLASS_URI);
            labelModifyListener.setExistingLabels(existingLabels);

            categoryComboForDescriptors.setInput(categories);
            categoryComboForDescriptors.getCombo().select(categories.size()-1);
            categoryComboForDescriptors.setSelection(categoryComboForDescriptors.getSelection());
            addDescriptorToTemplateAction.setEnabled(editable);
            deleteDescriptorFromTemplateAction.setEnabled(editable);
            tableViewer.getTable().setEnabled(editable);

            categoryChangedForDescriptorGroupsListener.setSelectedTemplate(selectedTemplate);
            categoryCombo.setInput(categories);
            categoryCombo.getCombo().select(categories.size()-1);
            categoryCombo.setSelection(categoryCombo.getSelection());
            addDescriptorGroupToTemplateAction.setEnabled(editable);
            deleteDescriptorGroupFromTemplateAction.setEnabled(editable);
            dgTableViewer.getTable().setEnabled(editable);
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Resetting right side Template section for the selection.");
    }


}
