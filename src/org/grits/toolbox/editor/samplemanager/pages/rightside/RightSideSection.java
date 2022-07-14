/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside;

import org.apache.log4j.Logger;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.grits.toolbox.core.dataShare.PropertyHandler;
import org.grits.toolbox.editor.samplemanager.config.Config;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.rightside.listeners.DescriptionModifyListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.listeners.LabelModifyListener;

/**
 * 
 *
 */
public abstract class RightSideSection
{
    private Logger logger = Logger.getLogger(RightSideSection.class);

    protected AbstractManagementPage page = null;
    protected FormToolkit toolkit = null;
    protected Section rightSideSection = null;
    protected Composite rightSideComposite;
    protected ClassesWithFeatures selectedObject;
    protected boolean editable = false;
    protected Font boldFont = null;
    protected Label labelLabel = null;
    protected Label labelDescription = null;
    protected Text labelText = null;
    protected Text descriptionText = null;
    protected LabelModifyListener labelModifyListener = null;
    protected DescriptionModifyListener descriptionModifyListener = null;
    //    protected ControlDecoration labelControlDecoration;

    public RightSideSection(AbstractManagementPage page)
    {
        this.page = page;
        this.rightSideSection  = page.getRightSectionOfPage();
        this.toolkit = page.getToolkit();
        Font currentFont = Display.getCurrent().getSystemFont();
        FontData fontData= currentFont.getFontData()[0];
        boldFont  = new Font(Display.getCurrent(),fontData.getName(), fontData.getHeight(), SWT.BOLD);
    }

    public void setSelectedObject(ClassesWithFeatures selectedObject)
    {
        logger.debug("- START : Setting selected object for the right side section.");
        try
        {
            if(selectedObject != null)
            {
                if(labelLabel == null)
                {
                    this.createGeneralPart();
                }
                this.selectedObject = selectedObject;
                this.editable = selectedObject.isEditable();
                this.reset();
            }
            else
            {
                rightSideSection.setText(Config.MANAGEMENT_RIGHT_SIDE_SECTION_NO_SELECTION_TITLE);
                rightSideSection.setExpanded(false);
            }
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Setting selected object for the right side section.");
    }

    protected void createGeneralPart()
    {
        logger.debug("- START : Creating part for the right side section.");
        try
        {
            rightSideComposite = toolkit.createComposite(rightSideSection, SWT.NONE);
            GridLayout rightSideCompositeLayout = new GridLayout();
            rightSideCompositeLayout.verticalSpacing = 30;
            rightSideCompositeLayout.horizontalSpacing = 10;
            rightSideCompositeLayout.numColumns = 4;
            rightSideCompositeLayout.makeColumnsEqualWidth = false;
            rightSideComposite.setLayout(rightSideCompositeLayout);

            this.createLineForLabel(rightSideComposite);
            this.createDescriptionLine(rightSideComposite);

            this.createSpecificPart();
            rightSideComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Creating part for the right side section.");
    }

    protected abstract void createSpecificPart();

    public void reset()
    {
        logger.debug("- START : Resetting part for the right side section.");
        try
        {
            rightSideSection.setExpanded(false);
            rightSideSection.setText(Config.MANAGEMENT_LABEL_SECTION_PREFIX + this.selectedObject.getLabel());
            labelText.setEnabled(editable);
            setSelectedObjectForLabelListener();
            labelText.setText(this.selectedObject.getLabel());

            this.resetDescriptionPart();

            this.resetSpecificPart();

            rightSideSection.setClient(rightSideComposite);
            rightSideSection.setExpanded(true);
            rightSideSection.update();
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Resetting part for the right side section.");
    }

    protected void resetDescriptionPart()
    {
        descriptionText.setEditable(editable);
        descriptionModifyListener.setSelectedObject(this.selectedObject);
        if(this.selectedObject.getDescription() != null)
            descriptionText.setText(this.selectedObject.getDescription());
        else
            descriptionText.setText("");
//        descriptionText.setLineJustify(0, descriptionText.getLineCount(), true);
    }

    protected abstract void resetSpecificPart();

    protected void createLineForLabel(Composite rightSideComposite)
    {
        labelLabel = toolkit.createLabel(rightSideComposite, "Label*");
        labelLabel.setFont(this.boldFont);
        GridData labelGridData = new GridData();
        labelGridData.widthHint = Config.MANAGEMENT_LABEL_LABEL_WIDTH;
        labelLabel.setLayoutData(labelGridData);

        labelText = toolkit.createText(rightSideComposite, "", SWT.FILL|SWT.WRAP);
        labelText.setTextLimit(PropertyHandler.LABEL_TEXT_LIMIT);

        setLabelModificationListener();
        GridData textGridData = new GridData();
        textGridData.grabExcessHorizontalSpace = true;
        textGridData.widthHint = Config.MANAGEMENT_LABEL_TEXT_WIDTH;
        textGridData.horizontalSpan = 3;
        labelText.setLayoutData(textGridData );
        toolkit.paintBordersFor(rightSideComposite);
    }

    protected void setLabelModificationListener()
    {
        ControlDecoration labelControlDecoration = new ControlDecoration(labelText, SWT.LEFT);
        labelModifyListener = new LabelModifyListener(this.page, labelControlDecoration);
        labelText.addModifyListener(labelModifyListener);
    }

    protected void setSelectedObjectForLabelListener()
    {
        labelModifyListener.setSelectedObject(this.selectedObject);
    }

    protected void createDescriptionLine(Composite rightSideComposite)
    {
        labelDescription = toolkit.createLabel(rightSideComposite, "Description");
        labelDescription.setFont(this.boldFont);
        labelDescription.setLayoutData(new GridData());
        descriptionText = new Text(rightSideComposite, SWT.BORDER|SWT.V_SCROLL|SWT.WRAP);
        descriptionText.setTextLimit(Config.MANAGEMENT_DESCRIPTION_TEXT_LIMIT);
        setDescriptionModificationListener();
        GridData descriptionGridData = new GridData();
        descriptionGridData.grabExcessHorizontalSpace = true;
        descriptionGridData.widthHint = Config.MANAGEMENT_DESCRIPTION_TEXT_WIDTH;
        descriptionGridData.heightHint = Config.MANAGEMENT_DESCRIPTION_TEXT_HEIGHT;
        descriptionGridData.horizontalSpan = 3;
        descriptionText.setLayoutData(descriptionGridData);
    }

    private void setDescriptionModificationListener()
    {
        descriptionModifyListener = new DescriptionModifyListener(this.page);
        descriptionText.addModifyListener(descriptionModifyListener);
        descriptionText.addKeyListener(descriptionModifyListener);
    }
}
