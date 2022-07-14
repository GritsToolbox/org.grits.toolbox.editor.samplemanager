/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.template;

import java.util.HashMap;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.grits.toolbox.entry.sample.model.Category;

/**
 * 
 *
 */
public class CategoryComboLabelProvider implements ILabelProvider
{
    private HashMap<String, String> uriLabelMap = null;
    
    public CategoryComboLabelProvider(HashMap<String, String> uriLabelMap)
    {
        this.uriLabelMap = uriLabelMap;
    }

    @Override
    public void addListener(ILabelProviderListener listener)
    {

    }

    @Override
    public void dispose()
    {
        
    }

    @Override
    public boolean isLabelProperty(Object element, String property)
    {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener)
    {
        
    }

    @Override
    public Image getImage(Object element)
    {
        return null;
    }

    @Override
    public String getText(Object element)
    {
        String label = null;
        if(element instanceof Category)
        {
//            if(!((Category) element).getUri().equals("Show All")){
                label = uriLabelMap.get(((Category) element).getUri());
//            }
//            else{
//                label = "Show All";
//            }
        }
        return label;
    }

}
