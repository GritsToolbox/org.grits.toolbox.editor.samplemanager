/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.config;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.grits.toolbox.editor.samplemanager.Activator;

/**
 * 
 *
 */
public class Config
{   

    /**
     *******************************************************************************************************
     *******************************************************************************************************
     *          Management Editor
     *******************************************************************************************************
     ********************************************************************************************************
     */

    /**
     ***********************************
     *          String Messages
     ***********************************
     */

    public static final String GRITS_MANAGEMENT_EDITOR = "Meta Data Manager";

    public static final String MANAGEMENT_LABEL_SECTION_PREFIX = "General Info : ";

    /**
    /***********************************
     *          Size
     ***********************************
     */

    public static final int MANAGEMENT_DESCRIPTION_TEXT_LIMIT = 10000;

    /**
     ***********************************
     *          Measurements
     ***********************************
     */
    public static final int MANAGEMENT_DESCRIPTOR_NAMESPACE_TABLE_WIDTH = 721;
    
    public static final int MANAGEMENT_DESCRIPTOR_NAMESPACE_TABLE_HEIGHT = 80;
    
    public static final int MANAGEMENT_DESCRIPTOR_UNIT_TABLE_WIDTH = 721;

    public static final int MANAGEMENT_DESCRIPTOR_UNIT_TABLE_HEIGHT = 120;

    public static final int MANAGEMENT_LABEL_TEXT_WIDTH = 550;

    public static final int MANAGEMENT_LABEL_LABEL_WIDTH = 168;

    public static final int MANAGEMENT_DESCRIPTION_TEXT_WIDTH = 540;

    public static final int MANAGEMENT_DESCRIPTION_TEXT_HEIGHT = 80;

    public static final int MANAGEMENT_CATEGORY_CHECKBOX_WIDTH = 360;

    public static final String MANAGEMENT_RIGHT_SIDE_SECTION_NO_SELECTION_TITLE = "No Selection    ";

    public static final int MANAGEMENT_NAMESPACE_SELECTION_COMBO_WIDTH = 350;

    public static final int MANAGEMENT_NAMESPACE_SELECTION_DIALOG_HEIGHT = 90;
    /**
     ***********************************
     *          Icons
     ***********************************
     */
    // http://www.findicons.com/icon/225869/gear
    // Alexander Moore GNU GPL license ​http://www.famfamfam.com
    public static final ImageDescriptor GRITS_MANAGEMENT_ICON = 
    		Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons" + File.separator + "gear.png");

    // http://www.findicons.com/icon/93451/old_view_refresh?id=95126
    // schollidesign GNU GPL http://www.schollidesign.deviantart.com
    public static final ImageDescriptor REFRESH_ICON = 
    		Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons" + File.separator + "refresh_icon.png");
}
