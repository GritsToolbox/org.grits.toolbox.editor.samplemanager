/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.dialogs;

import java.util.Comparator;

import org.grits.toolbox.entry.sample.model.Namespace;

/**
 * 
 *
 */
public class NamespaceLabelComparator implements Comparator<Namespace>
{

    @Override
    public int compare(Namespace n1, Namespace n2)
    {
        return n1.getLabel().compareToIgnoreCase(n2.getLabel());
    }
}
