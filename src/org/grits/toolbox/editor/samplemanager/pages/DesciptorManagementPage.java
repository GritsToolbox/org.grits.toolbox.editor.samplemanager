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
import org.grits.toolbox.editor.samplemanager.input.DescriptorWithFeatures;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.descriptortype.descriptor.DescriptorSelectionListener;
import org.grits.toolbox.entry.sample.model.Descriptor;

/**
 * 
 *
 */
public class DesciptorManagementPage extends AbstractManagementPage
{
    private Logger logger = Logger.getLogger(DesciptorManagementPage.class);

    public static final String ID = "0";
    public static final String PAGE_TITLE = "Descriptors";
    private List<DescriptorWithFeatures> inputDescriptors = null;

    public DesciptorManagementPage(FormEditor managementEditor)
    {
        super(managementEditor, ID, PAGE_TITLE);
        this.inputDescriptors = this.getAllDescriptorWithFeatures();
    }

    public List<DescriptorWithFeatures> getAllDescriptorWithFeatures()
    {
        List<DescriptorWithFeatures> descriptorWithFeaturesList = 
                new ArrayList<DescriptorWithFeatures>();
        logger.debug("- START : Retrieving all Descriptors for the Manager Editor.");
        try
        {
            ManagementEditor managementEditor = (ManagementEditor) this.getEditor();
            List<Descriptor> descriptors = null;
            List<String> standardDescriptorURIs = new ArrayList<String>();
            DescriptorWithFeatures descWithFeatures = null;

            if(!managementEditor.managerMode) {
                descriptors = managementEditor.getOntologyManagerApi().getAllDescriptors(
                        managementEditor.getStandardOntologyModel());
                for(Descriptor desc : descriptors)
                {
                    descWithFeatures = new DescriptorWithFeatures(desc);
                    descWithFeatures.setEditable(false);
                    descriptorWithFeaturesList.add(descWithFeatures);
                    standardDescriptorURIs.add(desc.getUri());
                }
            }
            descriptors = managementEditor.getOntologyManagerApi().getAllDescriptors(
                    managementEditor.getLocalOntologyModel());
            for(Descriptor desc : descriptors)
            {
                if(!standardDescriptorURIs.contains(desc.getUri()))
                {
                    descWithFeatures = new DescriptorWithFeatures(desc);
                    descWithFeatures.setEditable(true);
                    descriptorWithFeaturesList.add(descWithFeatures);
                }
            }
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Retrieving all Descriptors for the Manager Editor.");
        return descriptorWithFeaturesList;
    }

    @Override
    public void createRestOftheContent()
    {
        logger.debug("- START : Creating remaining part for the Desciptor page.");

        createToolBar(leftSideTableViewer, OntologyManager.DESCRIPTOR_CLASS_URI);

        logger.debug("- END   : Creating remaining part for the Desciptor page.");
    }

    protected Object getInput()
    {
        return this.inputDescriptors;
    }

    public void refreshPage(Object descWithFeatures)
    {


    }

    @Override
    protected RightSideSelectionListener getSelectionListener()
    {
        return new DescriptorSelectionListener(this);
    }

    @Override
    protected void addInputToList(List<ClassesWithFeatures> inputList,
            int selectionIndex, String label, String uri)
    {
        logger.debug("- START : Adding input to the Desciptor page.");
        try
        {
            Descriptor desc = new Descriptor();
            desc.setUri(uri);
            desc.setLabel(label);
            DescriptorWithFeatures descWithFeatures = new DescriptorWithFeatures(desc);
            descWithFeatures.setEditable(true);
            inputList.add(selectionIndex, descWithFeatures);
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Adding input to the Desciptor page.");
    }

    @Override
    public void refreshPage()
    {
        leftSideSelection = Math.max(leftSideTableViewer.getTable().getSelectionIndex(), 0);
        boolean ascendingOrder = sortAlphaNumericAction.getAscending();
        sortAlphaNumericAction.setAscending(!ascendingOrder);
        this.inputDescriptors = this.getAllDescriptorWithFeatures();
        leftSideTableViewer.setInput(this.inputDescriptors);
        if(leftSideTableViewer.getTable().getItemCount() > 0)
        {
            sortAlphaNumericAction.run();
            leftSideTableViewer.getTable().select(leftSideSelection);
            leftSideTableViewer.setSelection(leftSideTableViewer.getSelection());
        }
    }
}
