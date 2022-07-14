/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.category;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.category.AddDescriptorGroupToCategoryAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.category.AddDescriptorToCategoryAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.category.DeleteDescriptorFromCategoryAction;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.category.DeleteDescriptorGroupFromCategoryAction;
import org.grits.toolbox.editor.samplemanager.config.Config;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.general.CheckboxTableUtility;
import org.grits.toolbox.editor.samplemanager.pages.general.SectionUtility;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSection;
import org.grits.toolbox.editor.samplemanager.util.TableColumnTextComparator;
import org.grits.toolbox.entry.sample.model.Category;

/**
 * 
 *
 */
public class RightSideCategorySection extends RightSideSection
{
	private Logger logger = Logger.getLogger(RightSideCategorySection.class);

	private Category selectedCategory;
	private String labelForCategory = null;
	private HashMap<String, String> uriLabelMap;
	private AddDescriptorToCategoryAction addDescriptorToCategoryAction;
	private DeleteDescriptorFromCategoryAction deleteDescriptorFromCategoryAction;
	private Composite descriptorListSectionComposite;
	private CheckboxTableViewer tableViewer;
	private Section descriptorListSection;
	private Section descriptorGroupListSection;
	private CheckboxTableViewer dgTableViewer;
	private AddDescriptorGroupToCategoryAction addDescriptorGroupToCategoryAction;
	private DeleteDescriptorGroupFromCategoryAction deleteDescriptorGroupFromCategoryAction;
	private Composite descriptorGroupListSectionComposite;

	public RightSideCategorySection(AbstractManagementPage page, HashMap<String, String> uriLabelMap)
	{
		super(page);
		this.uriLabelMap = uriLabelMap;
	}

	protected void createLineForLabel(Composite rightSideComposite)
	{
		labelLabel = toolkit.createLabel(rightSideComposite, "Label*");
		labelLabel.setFont(this.boldFont);
		GridData labelGridData = new GridData();
		labelGridData.widthHint = Config.MANAGEMENT_LABEL_LABEL_WIDTH + 10;
		labelLabel.setLayoutData(labelGridData);
		labelForCategory = this.uriLabelMap.get(this.selectedCategory.getUri());
		labelText = toolkit.createText(rightSideComposite, labelForCategory, SWT.READ_ONLY|SWT.FILL|SWT.WRAP);
		//        labelText.removeModifyListener((ModifyListener) labelText.getListeners(SWT.Modify)[0]);
		//        labelModifyListener = new LabelModifyListener(this.page, this.selectedObject);
		//        labelText.addModifyListener(labelModifyListener);
		GridData textGridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		textGridData.grabExcessHorizontalSpace = true;
		textGridData.widthHint = Config.MANAGEMENT_LABEL_TEXT_WIDTH;
		textGridData.horizontalSpan = 3;
		textGridData.verticalSpan = 3;
		labelText.setLayoutData(textGridData );
	}


	protected void createDescriptionLine(Composite rightSideComposite)
	{

	}

	@Override
	protected void createSpecificPart()
	{
		logger.debug("- START : Creating specific part in right side category section.");
		try
		{
			this.createDescriptorTableSection();
			this.createDescriptorGroupTableSection();
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Creating specific part in right side category section.");
	}

	public void reset()
	{
		logger.debug("- START : Resetting right side Category section for the selection.");
		try{
			rightSideSection.setClient(rightSideComposite);
			rightSideSection.setExpanded(false);
			labelForCategory = uriLabelMap.get(selectedCategory.getUri());
			rightSideSection.setText("General Info : " + labelForCategory);

			labelText.setText(labelForCategory);
			rightSideSection.setExpanded(true);
			rightSideSection.update();

			this.resetSpecificPart();
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Resetting right side Category section for the selection.");
	}

	@Override
	protected void resetSpecificPart()
	{
		tableViewer.setInput(this.selectedCategory);
		dgTableViewer.setInput(this.selectedCategory);
	}


	private void createDescriptorTableSection()
	{

		descriptorListSection = SectionUtility.createSectionForCategory(toolkit, page.getRightComposite(), 
				"Descriptors");

		descriptorListSectionComposite = SectionUtility.getCompositeInsideSection(toolkit, descriptorListSection, 
				1, 10, 2, 2);

		tableViewer = CheckboxTableUtility.createCheckboxTableViewer(page,
				descriptorListSectionComposite, SWT.NONE, "Descriptors");
		tableViewer.setContentProvider(new CategoryDescriptorMembersContentProvider());
		tableViewer.setLabelProvider(new CategoryMemberLabelProvider());
		tableViewer.setComparator(new TableColumnTextComparator());

		TableColumn column0 = tableViewer.getTable().getColumn(0);
		column0.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				tableViewer.getTable().setSortColumn(column0);
				int direction = tableViewer.getTable().getSortDirection()
						== SWT.UP ? SWT.DOWN : SWT.UP;
				tableViewer.getTable().setSortDirection(direction);
				tableViewer.refresh();
			}
		});

		tableViewer.setInput(this.selectedCategory);
		TableWrapData tData = new TableWrapData();
		tData.heightHint = 200;
		tableViewer.getTable().setLayoutData(tData);

		ToolBarManager toolBarManager = new ToolBarManager(SWT.BALLOON);
		addDescriptorToCategoryAction = new AddDescriptorToCategoryAction(page, tableViewer);
		toolBarManager.add(addDescriptorToCategoryAction);
		deleteDescriptorFromCategoryAction = new DeleteDescriptorFromCategoryAction(page, tableViewer);
		toolBarManager.add(deleteDescriptorFromCategoryAction);
		ToolBar toolbar = toolBarManager.createControl(descriptorListSection);
		descriptorListSection.setTextClient(toolbar);
		descriptorListSection.update();
	}

	private void createDescriptorGroupTableSection()
	{

		descriptorGroupListSection = SectionUtility.createSectionForCategory(toolkit, page.getRightComposite(), 
				"Descriptor Groups");

		descriptorGroupListSectionComposite = SectionUtility.getCompositeInsideSection(toolkit, 
				descriptorGroupListSection, 1, 10, 2, 2);

		dgTableViewer = CheckboxTableUtility.createCheckboxTableViewer(page,
				descriptorGroupListSectionComposite, SWT.NONE, "Descriptor Groups");
		dgTableViewer.setContentProvider(new CategoryDescriptorGroupMembersContentProvider());
		dgTableViewer.setLabelProvider(new CategoryMemberLabelProvider());
		dgTableViewer.setComparator(new TableColumnTextComparator());

		TableColumn column0 = dgTableViewer.getTable().getColumn(0);
		column0.setText("Descriptor Group");
		column0.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				dgTableViewer.getTable().setSortColumn(column0);
				int direction = dgTableViewer.getTable().getSortDirection()
						== SWT.UP ? SWT.DOWN : SWT.UP;
				dgTableViewer.getTable().setSortDirection(direction);
				dgTableViewer.refresh();
			}
		});

		dgTableViewer.setInput(this.selectedCategory);

		TableWrapData tData = new TableWrapData();
		tData.heightHint = 200;
		dgTableViewer.getTable().setLayoutData(tData);

		ToolBarManager toolBarManager = new ToolBarManager(SWT.BALLOON);
		addDescriptorGroupToCategoryAction = new AddDescriptorGroupToCategoryAction(page, dgTableViewer);
		toolBarManager.add(addDescriptorGroupToCategoryAction);
		deleteDescriptorGroupFromCategoryAction = new DeleteDescriptorGroupFromCategoryAction(page, dgTableViewer);
		toolBarManager.add(deleteDescriptorGroupFromCategoryAction);
		ToolBar toolbar = toolBarManager.createControl(descriptorGroupListSection);
		descriptorGroupListSection.setTextClient(toolbar);
		descriptorGroupListSection.update();
	}

	public void setSelectedObject(Category selectedCategory)
	{
		logger.debug("- START : Setting Category for the right side.");
		try
		{
			this.selectedCategory = selectedCategory;
			if(labelLabel == null)
			{
				this.createGeneralPart();
			}
			this.reset();
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Setting Category for the right side.");
	}

}
