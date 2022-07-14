/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;

/**
 * 
 *
 */
public class UnitListLabelProvider implements ITableLabelProvider, ITableFontProvider
{
    private Font boldFont  = null;
    private String defaultUnitURI = null;

    public UnitListLabelProvider()
    {
        Font currentFont = Display.getCurrent().getSystemFont();
        FontData fontData= currentFont.getFontData()[0];
        boldFont = new Font(Display.getCurrent(),fontData.getName(), fontData.getHeight(), SWT.BOLD);
    }

    @Override
    public void addListener(ILabelProviderListener listener)
    {

    }
    
    public void setDefaultUnitURI(String defaultUnitURI)
    {
        this.defaultUnitURI = defaultUnitURI;
    }

    @Override
    public void dispose()
    {
        if(boldFont!= null && !boldFont.isDisposed())
            boldFont.dispose();
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
    public Image getColumnImage(Object element, int columnIndex)
    {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex)
    {
        if(element instanceof MeasurementUnit)
            switch(columnIndex)
            {
            case 0 :
                return  ((MeasurementUnit) element).getLabel();
            case 1 :
                return  ((MeasurementUnit) element).getUri();
            }
        return null;
    }

    @Override
    public Font getFont(Object element, int columnIndex)
    {

        if(element instanceof MeasurementUnit)
        {
            if(((MeasurementUnit) element).getUri().equals(defaultUnitURI))
            {
                return boldFont;
            }
        }
        return null;
    }

}
