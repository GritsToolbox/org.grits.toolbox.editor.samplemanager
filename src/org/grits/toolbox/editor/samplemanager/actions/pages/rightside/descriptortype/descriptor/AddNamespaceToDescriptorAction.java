/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractAddAction;
import org.grits.toolbox.editor.samplemanager.dialogs.AddNamespaceDialog;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.Namespace;

/**
 * 
 *
 */
public class AddNamespaceToDescriptorAction extends AbstractAddAction
{
    private AddNamespaceDialog selectionDialog;

    public AddNamespaceToDescriptorAction(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
        selectionDialog = new AddNamespaceDialog(this.page.getLeftSectionOfPage().getShell());
    }

    @Override
    public void run()
    {
        Descriptor descriptor = (Descriptor) tableViewer.getInput();
        List<Namespace> availableNamespaces = super.ontologyManagerApi.getAllNamespaces();
        selectionDialog.setAvailableNamespaces(availableNamespaces );
        selectionDialog.setDescriptor(descriptor);
        selectionDialog.open();
        if(selectionDialog.getReturnCode() == Window.OK)
        {
            descriptor.addNamespace(selectionDialog.getSelectedNamespace());
            super.ontologyManagerApi.addNamespaceToDescriptor(descriptor.getUri(), 
                    selectionDialog.getSelectedNamespace().getUri());
            this.refreshModifed();
        }
    }

}
