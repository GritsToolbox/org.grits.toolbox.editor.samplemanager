/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.dialogs;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

/**
 * 
 *
 */
public abstract class AbstractDescriptorTypeSelection extends
        FilteredItemsSelectionDialog
{

    protected List<String> notAllowedLabels;
    public boolean mandatory = false;
    public Integer maxOccurrence = null;
    private Button mandatoryCheckbox;
    private Spinner spinner;
    private Label spinnerLabel;
    private Label spinnerCommentLabel;
    private boolean mandatoryButtonRequired = true;
    
    public AbstractDescriptorTypeSelection(Shell shell)
    {
        super(shell);
    }

    public AbstractDescriptorTypeSelection(Shell shell, boolean mandatoryButtonRequired)
    {
        this(shell);
        this.mandatoryButtonRequired = mandatoryButtonRequired;
    }

    /**
     * @param notAllowedDescriptors the notAllowedDescriptors to set
     */
    public void setNotAllowedLabels(List<String> notAllowedLabels)
    {
        this.notAllowedLabels = notAllowedLabels;
    }

    
    @Override
    protected Control createExtendedContentArea(Composite parent)
    {
        Composite container = new Composite(parent, SWT.FILL);
        container.setLayout(new GridLayout(3, false));

        Font currentFont = Display.getCurrent().getSystemFont();
        FontData fontData= currentFont.getFontData()[0];
        Font boldFont = new Font(Display.getCurrent(),fontData.getName(), fontData.getHeight(), SWT.BOLD);

        if(mandatoryButtonRequired)
        {
            mandatoryCheckbox = new Button(container, SWT.CHECK|SWT.RIGHT_TO_LEFT);
            mandatoryCheckbox.setText("Mandatory        ");
            mandatoryCheckbox.setFont(boldFont);
            GridData mandatoryCheckboxLayoutData = new GridData();
            mandatoryCheckboxLayoutData.widthHint = 100;
            mandatoryCheckboxLayoutData.horizontalSpan = 3;
            mandatoryCheckboxLayoutData.heightHint = 50;
            mandatoryCheckbox.setLayoutData(mandatoryCheckboxLayoutData);
            
            spinnerLabel = new Label(container, SWT.RIGHT);
            spinnerLabel.setText(" Max Occurrence");
            spinnerLabel.setFont(boldFont);
            spinner = new Spinner(container, SWT.None);
            spinner.setToolTipText("Max Occurrence");
            spinnerCommentLabel = new Label(container, SWT.RIGHT);
            spinnerCommentLabel.setText("Select 0 for unbounded/Infinite max occurrence");
        }
        

        Label label = new Label(container, SWT.None);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2));
        
        return container;
    }

    @Override
    public String getElementName(Object item)
    {
        return ((String) item);
    }

    protected void okPressed() {

        if(spinner.getSelection() == 0)
        {
            maxOccurrence = null;
        }
        else
        {
            maxOccurrence = spinner.getSelection();
        }
        if(mandatoryButtonRequired)
            mandatory = mandatoryCheckbox.getSelection();
        super.okPressed();
    }
}
