/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.general;

import java.util.HashMap;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.entry.sample.config.ImageRegistry;
import org.grits.toolbox.entry.sample.config.ImageRegistry.SampleImage;
import org.grits.toolbox.entry.sample.model.Category;

/**
 * 
 *
 */
public class ListLabelProvider implements ILabelProvider, IFontProvider
{

    private Font boldFont  = null;
    private HashMap<String, String> categoryURILabelMap = null;
	private Image editableIcon = null;

	public ListLabelProvider()
	{
		editableIcon = ImageRegistry.getImageDescriptor(SampleImage.EDIT_DESCRIPTOR_ICON).createImage();
	}

    @Override
    public void addListener(ILabelProviderListener listener)
    {

    }

    @Override
    public void dispose()
    {
        if(boldFont!= null && !boldFont.isDisposed())
            boldFont.dispose();
        if(editableIcon != null && !editableIcon.isDisposed())
        	editableIcon.dispose();
    }

    @Override
    public boolean isLabelProperty(Object element, String property)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Image getImage(Object element)
    {
        if(element instanceof ClassesWithFeatures)
        {
            if(((ClassesWithFeatures) element).isEditable())
            {
                    return editableIcon ;
            }
        }
        return null;
    }

    @Override
    public String getText(Object element)
    {
        String label = null;
        if(element instanceof ClassesWithFeatures) {
            label = ((ClassesWithFeatures) element).getLabel();
        }
        else
            if(element instanceof Category)
            {if(categoryURILabelMap!=null)
                label =  this.categoryURILabelMap.get(((Category) element).getUri());
//                return ((Category) element).getUri();
            }
        return label;
    }

    @Override
    public Font getFont(Object element)
    {
        Font font = null;
        if(element instanceof ClassesWithFeatures) {

            if(((ClassesWithFeatures) element).isEditable()) {
            if(boldFont == null) {
                Font currentFont = Display.getCurrent().getSystemFont();
                FontData fontData= currentFont.getFontData()[0];
                boldFont = new Font(Display.getCurrent(),fontData.getName(), fontData.getHeight(), SWT.BOLD);
            }
            font = boldFont;
            }
        }
        return font;
    }
    
    public void setCategoryHashMap(HashMap<String, String> uriLabelMap)
    {
        this.categoryURILabelMap  = uriLabelMap;
    }
}
