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
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class DescriptorGroupSelectionDialog extends
AbstractDescriptorTypeSelection
{

    private List<DescriptorGroup> allDescriptorGroups;

    public DescriptorGroupSelectionDialog(Shell shell)
    {
        super(shell);
    }
    
    public void setAllDescriptorGroups(List<DescriptorGroup> allDescriptorGroups)
    {
        this.allDescriptorGroups = allDescriptorGroups;
    }

    public DescriptorGroupSelectionDialog(Shell shell, boolean mandatoryButtonRequired)
    {
        super(shell, mandatoryButtonRequired);
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
        progressMonitor.beginTask("Looking for Descriptor Groups..", allDescriptorGroups.size());
        for(DescriptorGroup descriptorGroup : allDescriptorGroups)
        {
            contentProvider.add(descriptorGroup.getLabel(), itemsFilter);
            progressMonitor.worked(1);
        }
        progressMonitor.done();
                    }

}
