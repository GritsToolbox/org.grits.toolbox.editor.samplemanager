/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.input;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.grits.toolbox.editor.samplemanager.config.Config;

/**
 * 
 *
 */
public class ManagementEditorInput implements IEditorInput
{

    private String localOntologyFileName = null;
    private String standardOntologyFileName = null;

    public ManagementEditorInput(List<String> fileNames)
    {
        this.standardOntologyFileName = fileNames.get(0);
        this.localOntologyFileName = fileNames.get(1);
    }

    public ManagementEditorInput(String localOntologyFileName)
    {
        this.localOntologyFileName = localOntologyFileName;
    }

    /**
     * @return the standardOntologyFileName
     */
    public String getStandardOntologyFileName()
    {
        return standardOntologyFileName;
    }

    /**
     * @return the fileName
     */
    public String getLocalOntologyFileName()
    {
        return localOntologyFileName;
    }

	@Override
    @SuppressWarnings({"unchecked","rawtypes"})
    public Object getAdapter(Class adapter)
    {
        return null;
    }

    @Override
    public boolean exists()
    {
        return true;
    }

    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return Config.GRITS_MANAGEMENT_ICON;
    }

    @Override
    public String getName()
    {
        return Config.GRITS_MANAGEMENT_EDITOR;
    }

    @Override
    public IPersistableElement getPersistable()
    {
        return null;
    }

    @Override
    public String getToolTipText()
    {
        return "Managing the Meta Data for Sample Editor";
    }


    public boolean equals(Object obj) {
        boolean matched = false;
        if(obj instanceof ManagementEditorInput) {
            if(this.getStandardOntologyFileName() != null)
                matched = this.getStandardOntologyFileName()
                .equals(((ManagementEditorInput) obj)
                        .getStandardOntologyFileName());
            else
                matched = this.getLocalOntologyFileName()
                .equals(((ManagementEditorInput) obj)
                        .getLocalOntologyFileName());
        }
        return matched;
    }

}
