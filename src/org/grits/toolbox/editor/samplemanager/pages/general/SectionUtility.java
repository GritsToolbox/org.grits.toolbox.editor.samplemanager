/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.general;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
public class SectionUtility
{

    public static Section createSectionForCategory(FormToolkit toolkit, Composite rightSideComposite, String headline)
    {
        Composite categoryComposite = toolkit.createComposite(rightSideComposite, SWT.FILL);
        TableWrapLayout categoryCompositeLayout = new TableWrapLayout();
        categoryCompositeLayout.numColumns = 1;
        categoryCompositeLayout.makeColumnsEqualWidth = true;
        categoryComposite.setLayout(categoryCompositeLayout);
        Section categorySection = toolkit.createSection(categoryComposite, Section.TITLE_BAR);

        categorySection.setText(headline);
        categorySection.setLayout(new TableWrapLayout());
//        
//        ToolBarManager toolBarManager = new ToolBarManager(SWT.BALLOON);
//        ToolBar toolbar = toolBarManager.createControl(categorySection);
//        categorySection.setTextClient(toolbar);
        
        TableWrapData sectionData = new TableWrapData(TableWrapData.FILL_GRAB);
        sectionData.colspan = 1;
        categorySection.setLayoutData(sectionData);

        GridData compositeLayoutData = new GridData();
        compositeLayoutData.horizontalSpan = 4;
        categoryComposite.setLayoutData(compositeLayoutData);
        
        return categorySection;
    }

    public static Composite getCompositeInsideSection(FormToolkit toolkit, Section categorySection, 
            int numberOfColumns, int horizontalSpacing, int verticalSpacing, int rightMargin)
    {
        Composite categorySectionComposite = toolkit.createComposite(categorySection, SWT.FILL);

        TableWrapLayout sectionCompositeLayout = new TableWrapLayout();
        sectionCompositeLayout.numColumns = numberOfColumns;
        sectionCompositeLayout.horizontalSpacing = horizontalSpacing;
        sectionCompositeLayout.verticalSpacing = verticalSpacing;
        sectionCompositeLayout.rightMargin = rightMargin;
        sectionCompositeLayout.makeColumnsEqualWidth = true;
        categorySectionComposite.setLayout(sectionCompositeLayout);

        TableWrapData section1LayoutData = new TableWrapData(TableWrapData.FILL_GRAB);
        section1LayoutData.colspan = 1;
        section1LayoutData.grabVertical = true;
        categorySectionComposite.setLayoutData(section1LayoutData);
        categorySection.setClient(categorySectionComposite);
        return categorySectionComposite;
    }

}

