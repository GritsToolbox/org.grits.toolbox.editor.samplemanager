/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages.rightside;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;

/**
 * 
 *
 */
public abstract class RightSideSelectionListener implements ISelectionChangedListener
{
    protected RightSideSection rightSideSection = null;

    @Override
    public void selectionChanged(SelectionChangedEvent event)
    {
        TableViewer tableViewer = (TableViewer) event.getSource();
        ClassesWithFeatures selectedObject = (ClassesWithFeatures) tableViewer
                .getElementAt(tableViewer.getTable().getSelectionIndex());

        setSelection(selectedObject);
    }

    protected abstract void setSelection(ClassesWithFeatures selectedObject);
}
