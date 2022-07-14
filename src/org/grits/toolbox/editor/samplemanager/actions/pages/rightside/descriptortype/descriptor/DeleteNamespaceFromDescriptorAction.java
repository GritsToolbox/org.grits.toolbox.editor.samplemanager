/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions.pages.rightside.descriptortype.descriptor;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.actions.pages.rightside.AbstractDeleteAction;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.Namespace;

/**
 * 
 *
 */
public class DeleteNamespaceFromDescriptorAction extends AbstractDeleteAction
{

//    private String uri;
    
    public DeleteNamespaceFromDescriptorAction(AbstractManagementPage page,
            TableViewer tableViewer)
    {
        super(page, tableViewer);
    }

    /* (non-Javadoc)
     * @see org.grits.toolbox.editor.samplemanager.pages.general.AbstractDeleteAction#run()
     */
    @Override
    public void run()
    {
        Descriptor descriptor = (Descriptor) tableViewer.getInput();
        selectionIndex = this.tableViewer.getTable().getSelectionIndex();
        if(selectionIndex != -1)
        {
            Namespace selectedNamespace = (Namespace) tableViewer.getTable().getItem(selectionIndex).getData();
            List<Namespace> namespaces = descriptor.getNamespaces();
            int index = -1;
            for(Namespace namespace : namespaces)
            {
                if(namespace.getUri().equals(selectedNamespace.getUri()))
                {
                    index = namespaces.indexOf(namespace);
                    break;
                }
            }
            if(index != -1)
            {
                namespaces.remove(index);
                this.removeTripleFromOntology(descriptor.getUri(), "has_namespace", selectedNamespace.getUri());
            }
        }
    }

    public void setDescriptorUri(String uri)
    {
//        this.uri = uri;
    }

}
