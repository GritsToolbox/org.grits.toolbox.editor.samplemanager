/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.input.DescriptorTypeClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.general.SectionUtility;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSection;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.listeners.AbundanceListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.listeners.CheckboxSelectionListener;
import org.grits.toolbox.editor.samplemanager.util.CategoryURIComparator;

/**
 * 
 *
 */
public abstract class RightSideDescriptorTypeSection extends RightSideSection
{
    private Logger logger = Logger.getLogger(RightSideDescriptorTypeSection.class);

    private Label abundanceLabel = null;
    private Spinner abundanceSpinner = null;
    private AbundanceListener abundanceModifyListener = null;
    private List<Button> categoriesCheckboxes = null;
    private HashMap<String, String> categories;
    private HashMap<String, String> categoriesCheckBoxToURI;
    private Composite categorySectionComposite;

    public RightSideDescriptorTypeSection(AbstractManagementPage page)
    {
        super(page);
    }

    protected void createSpecificPart() 
    {
        logger.debug("- START : Creating specific part in abstract Descriptor type section.");
        try
        {
            abundanceLabel  = toolkit.createLabel(rightSideComposite, "Default Max Occurrence");
            abundanceLabel.setFont(this.boldFont);
            abundanceSpinner  = new Spinner(rightSideComposite, SWT.BORDER|SWT.READ_ONLY);
            abundanceModifyListener = new AbundanceListener(this.page);
            abundanceSpinner.addModifyListener(abundanceModifyListener);
            GridData labelGridData = new GridData();
            labelGridData.grabExcessHorizontalSpace = false;
            labelGridData.horizontalSpan = 2;
            abundanceSpinner.setLayoutData(labelGridData);
            toolkit.createLabel(rightSideComposite, "( Select 0 for unbounded/infinite maxOccurence )"); 

            Section categorySection = SectionUtility.createSectionForCategory(toolkit, page.getRightComposite(), "Categories");
            categorySectionComposite = toolkit.createComposite(categorySection, SWT.FILL);
            categorySectionComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
            GridLayout compositeLayout = new GridLayout(2,  true);
            compositeLayout.marginLeft = 10;
            compositeLayout.marginBottom = 5;
            compositeLayout.marginRight = 10;

            categorySectionComposite.setLayout(compositeLayout);

            categorySection.setClient(categorySectionComposite);
            categoriesCheckboxes = new ArrayList<Button>();

            categories = ((ManagementEditor) this.page.getEditor()).getAllCategoriesURILabelMap();
            categoriesCheckBoxToURI = new HashMap<String, String>();
            Button categoriesCheckbox;
            List<String> sortedCategory = new ArrayList<String>(categories.keySet());
            Collections.sort(sortedCategory, new CategoryURIComparator());
            for(String category : sortedCategory)
            {
                categoriesCheckbox = toolkit.createButton(categorySectionComposite, categories.get(category), SWT.CHECK);
                categoriesCheckbox.addSelectionListener(new CheckboxSelectionListener(this.page, category));
                categoriesCheckboxes.add(categoriesCheckbox);
                categoriesCheckBoxToURI.put(categoriesCheckbox.toString(), category);
                GridData layoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
                layoutData.horizontalSpan = 1;
                layoutData.widthHint = 360;
                categoriesCheckbox.setLayoutData(layoutData);
            }
            this.createMoreSpecificPart();
        } catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
        logger.debug("- END   : Creating specific part in abstract Descriptor type section.");
    }

    protected abstract void createMoreSpecificPart();

    @Override
    protected void resetSpecificPart()
    {
        logger.debug("- START : Resetting right side abstract Descriptor type section for the selection.");
        try
        {
            DescriptorTypeClassesWithFeatures selectedDescriptorTypeObject = ((DescriptorTypeClassesWithFeatures)this.selectedObject);
            abundanceSpinner.setEnabled(editable);
            abundanceModifyListener.setSelectedObject(selectedDescriptorTypeObject);
            if(selectedDescriptorTypeObject.getMaxOccurrence() != null)
                abundanceSpinner.setSelection(selectedDescriptorTypeObject.getMaxOccurrence());
            else
            {
                abundanceSpinner.setSelection(0);
            }

            boolean selected = false;
            Listener[] listeners = null;
            TypedListener typedListener = null;
            CheckboxSelectionListener checkboxListener = null; 
            for(Button checkbox : categoriesCheckboxes)
            {
                listeners = checkbox.getListeners(SWT.Selection);
                typedListener = (TypedListener) listeners[0];
                checkboxListener = (CheckboxSelectionListener) typedListener.getEventListener();
                checkboxListener.setSelectedObject(selectedDescriptorTypeObject);
                for(String category : categories.keySet())
                {
                    selected = false;
                    if(categoriesCheckBoxToURI.get(checkbox.toString()).equals(category))
                    {
                        if(selectedDescriptorTypeObject.getCategories() != null
                                && selectedDescriptorTypeObject.getCategories().contains(category))
                        {
                            selected = true;
                            break;
                        }
                    }
                }
                checkbox.setSelection(selected);
                checkbox.setEnabled(editable);
            }
            this.resetMoreSpecificPart();
        } catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
        logger.debug("- END   : Resetting right side abstract Descriptor type section for the selection.");
    }

    protected abstract void resetMoreSpecificPart();
}
