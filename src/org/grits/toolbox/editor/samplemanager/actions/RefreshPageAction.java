/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.editor.samplemanager.config.Config;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;

/**
 * 
 *
 */
public class RefreshPageAction extends Action
{
    protected AbstractManagementPage page = null;
    
    public RefreshPageAction(AbstractManagementPage page)
    {
        this.page = page;
        this.setupUI();
    }

    protected void setupUI() {
        this.setToolTipText("Refresh this page");
        ImageDescriptor imageDesc = Config.REFRESH_ICON;
        Image image = imageDesc.createImage();
        this.setImageDescriptor(ImageDescriptor.createFromImage(image));
    }

    public void run()
    {
        this.page.refreshPage();
    }

}
