/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.dialogs;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;

/**
 * 
 *
 */
public class UnitSelectionDialog extends FilteredItemsSelectionDialog
{
    private List<MeasurementUnit> allUnits = null;
    private List<String> notAllowedLabels = null;

    public UnitSelectionDialog(Shell parentShell, List<MeasurementUnit> allUnits)
    {
        super(parentShell);
        this.allUnits  = allUnits;
    }

    /**
     * @param notAllowedDescriptors the notAllowedDescriptors to set
     */
    public void setNotAllowedLabels(List<String> notAllowedLabels)
    {
        this.notAllowedLabels  = notAllowedLabels;
    }

    @Override
    protected Control createExtendedContentArea(Composite parent)
    {
        return null;
        
    }

    @Override
    protected IDialogSettings getDialogSettings()
    {
        return new DialogSettings("");
    }

    @Override
    protected IStatus validateItem(Object item)
    {
        return Status.OK_STATUS;
    }

    @Override
    protected ItemsFilter createFilter()
    {
        return new ItemsFilter() {
            public boolean matchItem(Object item) {
                String label = (String) item;
                return (!notAllowedLabels.contains(label) && matches(label));
            }
            public boolean isConsistentItem(Object item) {
                return true;
            }
        };
    }

    @Override
    protected Comparator<String> getItemsComparator()
    {
        return new Comparator<String>() {
            public int compare(String m1, String m2) 
            {
                return m1.compareTo(m2);
            }
         };
    }

    @Override
    protected void fillContentProvider(AbstractContentProvider contentProvider,
            ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
            throws CoreException
    {
        progressMonitor.beginTask("Looking for Units..", allUnits.size());
        for(MeasurementUnit unit : allUnits)
        {
                contentProvider.add(unit.getLabel(), itemsFilter);
                progressMonitor.worked(1);
        }
        progressMonitor.done();
    }

    @Override
    public String getElementName(Object item)
    {
        return ((String) item);
    }

}
