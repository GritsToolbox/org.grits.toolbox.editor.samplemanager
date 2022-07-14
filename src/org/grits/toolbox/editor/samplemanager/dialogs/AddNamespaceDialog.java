package org.grits.toolbox.editor.samplemanager.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.grits.toolbox.editor.samplemanager.config.Config;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor.NamespaceComboLabelProvider;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor.NamespaceContentProvider;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.Namespace;

public class AddNamespaceDialog extends Dialog
{
    private List<Namespace> availableNamespaces = null;
    private Descriptor descriptor;
    private ComboViewer namespaceCombo;
    private Namespace selectedNamespace;

    public AddNamespaceDialog(Shell parent)
    {
        super(parent);
    }
    
    /**
     * @param availableNamespaces the availableNamespaces to set
     */
    public void setAvailableNamespaces(List<Namespace> availableNamespaces)
    {
        this.availableNamespaces = availableNamespaces;
    }

    /**
     * @return the selectedNamespace
     */
    public Namespace getSelectedNamespace()
    {
        return selectedNamespace;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        parent.getShell().setText("Namespace Selection for : " + descriptor.getLabel());
        Composite container = new Composite(parent, SWT.FILL);
        container.setLayout(new GridLayout(2, false));
        
        Label namespaceLabel = new Label(container, SWT.FILL);
        namespaceLabel.setText("Choose a namespace");
        namespaceLabel.setLayoutData(new GridData());
        
        namespaceCombo = new ComboViewer(container, SWT.READ_ONLY);
        namespaceCombo.setContentProvider(new NamespaceContentProvider());
        namespaceCombo.setLabelProvider(new NamespaceComboLabelProvider());
        List<String> addedURIs = new ArrayList<String>();
        for(Namespace ns : descriptor.getNamespaces())
        {
            addedURIs.add(ns.getUri());
        }

        List<Namespace> availableNamespaces = new ArrayList<Namespace>();
        for(Namespace namespace : this.availableNamespaces)
        {
            if(!addedURIs.contains(namespace.getUri()))
                availableNamespaces.add(namespace);
        }
        Collections.sort(availableNamespaces, new NamespaceLabelComparator());
        namespaceCombo.setInput(availableNamespaces);
        GridData gd = new GridData();
        gd.widthHint = Config.MANAGEMENT_NAMESPACE_SELECTION_COMBO_WIDTH;
        namespaceCombo.getCombo().setLayoutData(gd);
        GridData containerData = new GridData();
        containerData.heightHint = Config.MANAGEMENT_NAMESPACE_SELECTION_DIALOG_HEIGHT;
        container.setLayoutData(containerData);
        return container;
    }
    
    public void setDescriptor(Descriptor descriptor)
    {
        this.descriptor = descriptor;
    }
    
    protected void okPressed()
    {
        int index = namespaceCombo.getCombo().getSelectionIndex();
        if(index >= 0)
        {
            selectedNamespace = (Namespace) namespaceCombo.getElementAt(index);
            super.okPressed();
        }
        else
        {
            MessageDialog.openWarning(getParentShell(), "No Namespace selected", 
                    "You pressed OK without selecting a namespace. "
                    + "Please select a namespace or else press CANCEL.");
        }
    }
}
