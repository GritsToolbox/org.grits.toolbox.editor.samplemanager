/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.ui.forms.editor.FormEditor;
import org.grits.toolbox.editor.samplemanager.ManagementEditor;
import org.grits.toolbox.editor.samplemanager.input.ClassesWithFeatures;
import org.grits.toolbox.editor.samplemanager.input.DescriptorGroupWithFeatures;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptorgroup.DescriptorGroupSelectionListener;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;

/**
 * 
 *
 */
public class DescriptorGroupManagementPage extends AbstractManagementPage
{
    private Logger logger = Logger.getLogger(DescriptorGroupManagementPage.class);

    public static final String ID = "1";
    public static final String PAGE_TITLE = "Descriptor Groups";
    public List<DescriptorGroupWithFeatures> inputDescriptorGroups = null;

    public DescriptorGroupManagementPage(FormEditor managementEditor)
    {
        super(managementEditor, ID, PAGE_TITLE);
        this.inputDescriptorGroups  = this.getAllDescriptorGroupWithFeatures();
    }

    public List<DescriptorGroupWithFeatures> getAllDescriptorGroupWithFeatures()
    {
        List<DescriptorGroupWithFeatures> descriptorGroupWithFeaturesList = 
                new ArrayList<DescriptorGroupWithFeatures>();
        logger.debug("- START : Retrieving all Descriptor Groups for the Manager Editor.");
        try
        {
            ManagementEditor managementEditor = (ManagementEditor) this.getEditor();
            List<DescriptorGroup> descriptorGroups = null;
            List<String> standardDescriptorGroupURIs = new ArrayList<String>();
            DescriptorGroupWithFeatures descWithFeatures = null;

            if(!managementEditor.managerMode) {
                descriptorGroups = managementEditor.getOntologyManagerApi().getAllDescriptorGroups(
                        managementEditor.getStandardOntologyModel());
                for(DescriptorGroup dg : descriptorGroups)
                {
                    descWithFeatures = new DescriptorGroupWithFeatures(dg);
                    descWithFeatures.setEditable(false);
                    descriptorGroupWithFeaturesList.add(descWithFeatures);
                    standardDescriptorGroupURIs.add(dg.getUri());
                }
            }
            descriptorGroups = managementEditor.getOntologyManagerApi().getAllDescriptorGroups(
                    managementEditor.getLocalOntologyModel());
            for(DescriptorGroup dg : descriptorGroups)
            {
                if(!standardDescriptorGroupURIs.contains(dg.getUri()))
                {
                    descWithFeatures = new DescriptorGroupWithFeatures(dg);
                    descWithFeatures.setEditable(true);
                    descriptorGroupWithFeaturesList.add(descWithFeatures);
                }
            }
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Retrieving all Descriptor Groups for the Manager Editor.");
        return descriptorGroupWithFeaturesList;
    }

    @Override
    public void createRestOftheContent()
    {
        logger.debug("- START : Creating remaining part for the Desciptor Group page.");

        createToolBar(leftSideTableViewer, OntologyManager.DESCRIPTOR_GROUP_CLASS_URI);

        logger.debug("- END   : Creating remaining part for the Desciptor Group page.");

    }

    @Override
    protected RightSideSelectionListener getSelectionListener()
    {
        return new DescriptorGroupSelectionListener(this);
    }

    @Override
    protected Object getInput()
    {
        return this.inputDescriptorGroups;
    }

    @Override
    protected void addInputToList(List<ClassesWithFeatures> inputList,
            int selectionIndex, String label, String uri)
    {
        logger.debug("- START : Adding input to the Desciptor Group page.");
        try
        {
            DescriptorGroup dg = new DescriptorGroup();
            dg.setLabel(label);
            dg.setUri(uri);
            DescriptorGroupWithFeatures descGroupWithFeatures = new DescriptorGroupWithFeatures(dg);
            descGroupWithFeatures.setEditable(true);
            inputList.add(selectionIndex, descGroupWithFeatures);
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Adding input to the Desciptor Group page.");
    }
    @Override
    public void refreshPage()
    {
        leftSideSelection = Math.max(leftSideTableViewer.getTable().getSelectionIndex(), 0);
        boolean ascendingOrder = sortAlphaNumericAction.getAscending();
        sortAlphaNumericAction.setAscending(!ascendingOrder);
        this.inputDescriptorGroups = this.getAllDescriptorGroupWithFeatures();
        leftSideTableViewer.setInput(this.inputDescriptorGroups);
        if(leftSideTableViewer.getTable().getItemCount() > 0)
        {
            sortAlphaNumericAction.run();
            leftSideTableViewer.getTable().select(leftSideSelection);
            leftSideTableViewer.setSelection(leftSideTableViewer.getSelection());
        }
    }

}
