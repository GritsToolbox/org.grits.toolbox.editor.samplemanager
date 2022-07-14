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
import org.eclipse.swt.widgets.Shell;
import org.grits.toolbox.entry.sample.model.Descriptor;

/**
 * 
 *
 */
public class DescriptorSelectionDialog extends AbstractDescriptorTypeSelection
{
    private List<Descriptor> allDescriptors = null;

    public DescriptorSelectionDialog(Shell parentShell)
    {
        super(parentShell);
    }

    public void setAllDescriptors(List<Descriptor> allDescriptors)
    {
        this.allDescriptors = allDescriptors;
    }

    public DescriptorSelectionDialog(Shell parentShell, 
            boolean mandatoryButtonRequired)
    {
        super(parentShell, mandatoryButtonRequired);
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
        progressMonitor.beginTask("Looking for Descriptors..", allDescriptors.size());
        for(Descriptor descriptor : allDescriptors)
        {
                contentProvider.add(descriptor.getLabel(), itemsFilter);
                progressMonitor.worked(1);
        }
        progressMonitor.done();
    }

}
