/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.config.Config;
import org.grits.toolbox.editor.samplemanager.ontology.GritsOntologyManagerApi;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public class LabelModifyListener extends AbstractModificationListener implements ModifyListener
{
    private List<String> existingLabels;
    private ControlDecoration controlDecoration;
    private Image errorImage;

    public LabelModifyListener(AbstractManagementPage page, ControlDecoration controlDecoration)
    {
        super(page);
        this.controlDecoration = controlDecoration;
        errorImage = FieldDecorationRegistry.getDefault()
                .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
                .getImage();
        this.controlDecoration.setMarginWidth(2);
        this.controlDecoration.setImage(errorImage);
        this.controlDecoration.hide();
    }

    @Override
    public void modifyText(ModifyEvent e)
    {
        this.existingLabels.remove(initialLabel);
        Text text = (Text) e.getSource();
        String textValue = text.getText().trim();
        String newLabel = textValue.isEmpty() ? null : textValue;
        if(newLabel != null && !this.selectedObject.getLabel().equals(newLabel))
        {
            if(this.existingLabels.contains(newLabel))
            {
                controlDecoration.setDescriptionText("  Please choose a unique label."
                        + " This label cannot be assigned.  \n"
                        + "  The label is currently set as :    \"" 
                        + this.selectedObject.getLabel()
                        + "\"  \n");
                controlDecoration.show();
            }
            else
            {
                this.selectedObject.setLabel(newLabel);
                this.update(newLabel);
                controlDecoration.hide();
                page.getLeftSideTableViewer().refresh();
                page.getRightSectionOfPage().setText(Config.MANAGEMENT_LABEL_SECTION_PREFIX + this.selectedObject.getLabel());
            }
        }
        else if(newLabel== null)
        {
            controlDecoration.setDescriptionText("  Please choose a non-empty label."
                    + " This label cannot be assigned.  \n"
                    + "  The label is currently set as :    \"" 
                    + this.selectedObject.getLabel()
                    + "\"  \n");
            controlDecoration.show();
        }
        else{
            controlDecoration.hide();
        }
    }

    protected void updateDataSourcePart(Object newLabel)
    {
        GritsOntologyManagerApi ontoManagerApi = ((ManagementEditor) this.page.getEditor()).getOntologyManagerApi();
        ontoManagerApi.updateLabel(this.selectedObject.getUri(), (String) newLabel);
    }

    public void setExistingLabels(List<String> existingLabels)
    {
        this.existingLabels = new ArrayList<String>(existingLabels);
    }

}
