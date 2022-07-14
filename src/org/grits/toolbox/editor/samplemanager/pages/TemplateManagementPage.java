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
import org.grits.toolbox.editor.samplemanager.input.TemplateWithFeatures;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.editor.samplemanager.pages.rightside.RightSideSelectionListener;
import org.grits.toolbox.editor.samplemanager.pages.rightside.template.TemplateSelectionListener;
import org.grits.toolbox.entry.sample.model.Template;

/**
 * 
 *
 */
public class TemplateManagementPage extends AbstractManagementPage
{
    private Logger logger = Logger.getLogger(TemplateManagementPage.class);

    public static final String ID = "2";
    public static final String PAGE_TITLE = "Templates";
    private List<TemplateWithFeatures> inputTemplates = null;

    public TemplateManagementPage(FormEditor managementEditor)
    {
        super(managementEditor, ID, PAGE_TITLE);
        this.inputTemplates = this.getAllTemplates();
    }

    private List<TemplateWithFeatures> getAllTemplates()
    {
        List<TemplateWithFeatures> templateWithFeaturesList = 
                new ArrayList<TemplateWithFeatures>();
        logger.debug("- START : Retrieving all Templates for the Manager Editor.");
        try
        {
            ManagementEditor managementEditor = (ManagementEditor) this.getEditor();
            List<Template> templates = null;
            List<String> standardTemplateURIs = new ArrayList<String>();
            TemplateWithFeatures descWithFeatures = null;
            if(!managementEditor.managerMode) {
                templates = managementEditor.getOntologyManagerApi().getAllTemplates(
                        managementEditor.getStandardOntologyModel());
                for(Template temp : templates)
                {
                    descWithFeatures = new TemplateWithFeatures(temp);
                    descWithFeatures.setEditable(false);
                    templateWithFeaturesList.add(descWithFeatures);
                    standardTemplateURIs.add(temp.getUri());
                }
            }
            templates = managementEditor.getOntologyManagerApi().getAllTemplates(
                    managementEditor.getLocalOntologyModel());
            for(Template temp : templates)
            {
                if(!standardTemplateURIs.contains(temp.getUri()))
                {
                    descWithFeatures = new TemplateWithFeatures(temp);
                    descWithFeatures.setEditable(true);
                    templateWithFeaturesList.add(descWithFeatures);
                }
            }
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Retrieving all Templates for the Manager Editor.");
        return templateWithFeaturesList;
    }

    @Override
    public void createRestOftheContent()
    {
        logger.debug("- START : Creating remaining part for the Template page.");

        createToolBar(leftSideTableViewer, OntologyManager.TEMPLATE_CLASS_URI);

        logger.debug("- END   : Creating remaining part for the Template page.");
    }

    @Override
    protected RightSideSelectionListener getSelectionListener()
    {
        return new TemplateSelectionListener(this);
    }

    @Override
    protected Object getInput()
    {
        return this.inputTemplates;
    }

    @Override
    protected void addInputToList(List<ClassesWithFeatures> inputList,
            int selectionIndex, String label, String uri)
    {
        logger.debug("- START : Adding input to the Template page.");
        try
        {
            Template temp = new Template();
            temp.setUri(uri);
            temp.setLabel(label);
            TemplateWithFeatures tempWithFeatures = new TemplateWithFeatures(temp);
            tempWithFeatures.setEditable(true);
            inputList.add(selectionIndex, tempWithFeatures);
        } catch (Exception ex)
        {
            logger.error(ex);
        }
        logger.debug("- END   : Adding input to the Template page.");
    }

    @Override
    public void refreshPage()
    {
        leftSideSelection = Math.max(leftSideTableViewer.getTable().getSelectionIndex(), 0);
        boolean ascendingOrder = sortAlphaNumericAction.getAscending();
        sortAlphaNumericAction.setAscending(!ascendingOrder);
        this.inputTemplates = this.getAllTemplates();
        leftSideTableViewer.setInput(this.inputTemplates);
        if(leftSideTableViewer.getTable().getItemCount() > 0)
        {
            sortAlphaNumericAction.run();
            leftSideTableViewer.getTable().select(leftSideSelection);
            leftSideTableViewer.setSelection(leftSideTableViewer.getSelection());
        }
    }

}
