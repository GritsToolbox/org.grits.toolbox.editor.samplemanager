/**
 * 
 */
package org.grits.toolbox.editor.samplemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.grits.toolbox.editor.samplemanager.config.Config;
import org.grits.toolbox.editor.samplemanager.input.ManagementEditorInput;
import org.grits.toolbox.editor.samplemanager.ontology.GritsOntologyManagerApi;
import org.grits.toolbox.editor.samplemanager.ontology.OntologyManager;
import org.grits.toolbox.editor.samplemanager.pages.AbstractManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.CategoryManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.DesciptorManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.DescriptorGroupManagementPage;
import org.grits.toolbox.editor.samplemanager.pages.TemplateManagementPage;
import org.grits.toolbox.editor.samplemanager.util.ModelValidator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * 
 *
 */
public class ManagementEditor extends FormEditor
{
    private static final Logger logger = Logger.getLogger(ManagementEditor.class);
    public static final String EDITOR_ID = "org.grits.toolbox.editor.samplemanager.managementEditor";

    private OntModel localOntologyModel = null;
    private OntModel standardOntologyModel = null;
    private GritsOntologyManagerApi ontologyManagerApi= null;
    public boolean managerMode = false;
    private boolean dirtyFlag = false;

    public void init(IEditorSite site, IEditorInput input) throws PartInitException
    {
        logger.debug("- START : Initializing the Sample Meta Data Manager Editor.");
        try
        {
            super.init(site, input);
            String localOntologyFile = ((ManagementEditorInput) input).getLocalOntologyFileName();
            FileInputStream localInputOntology = new FileInputStream(localOntologyFile);
            this.createLocalOntModel(localInputOntology);
            String standardOntologyFile = ((ManagementEditorInput) input).getStandardOntologyFileName();
            localInputOntology.close();
            if(standardOntologyFile == null) 
            {
                setPartName(Config.GRITS_MANAGEMENT_EDITOR + " - Admin");
                managerMode = true;
                this.ontologyManagerApi = new GritsOntologyManagerApi(this.localOntologyModel);
            }
            else 
            {
                setPartName(Config.GRITS_MANAGEMENT_EDITOR + " - User");
                FileInputStream standardInputOntology = new FileInputStream(standardOntologyFile);
                this.createStandardOntModel(standardInputOntology);
                this.ontologyManagerApi = new GritsOntologyManagerApi(this.standardOntologyModel, this.localOntologyModel);
                standardInputOntology.close();
            }
        } catch (Exception ex)  {
            logger.error("Error while reading ontology file", ex);
            throw new PartInitException(ex.getMessage());
        }
        logger.debug("- END   : Initializing the Sample Meta Data Manager Editor.");
    }

    private void createStandardOntModel(FileInputStream standardInputOntology)
    {
        logger.debug("- START : Creating Standard Ontology Model.");

        this.standardOntologyModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);
        this.standardOntologyModel.read(standardInputOntology, OntologyManager.baseURI);

        logger.debug("- END   : Creating Standard Ontology Model.");
    }

    private void createLocalOntModel(FileInputStream locaInputOntology) throws FileNotFoundException
    {
        logger.debug("- START : Creating Local Ontology Model.");

        this.localOntologyModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);
        this.localOntologyModel.read(locaInputOntology, OntologyManager.baseURI);

        logger.debug("- END   : Creating Local Ontology Model.");
    }
    
    /**
     * @return the ontologyManagerApi
     */
    public GritsOntologyManagerApi getOntologyManagerApi()
    {
        return ontologyManagerApi;
    }

    /**
     * @return the standardOntologyModel
     */
    public OntModel getStandardOntologyModel()
    {
        return standardOntologyModel;
    }

    public OntModel getLocalOntologyModel()
    {
        return this.localOntologyModel;
    }

    public void synchronizingFileWithModel() throws Exception
    {
        logger.debug("- START : Synchronizing ontology file with model.");
        try
        {
            String currentFileName = ((ManagementEditorInput)this.getEditorInput()).getLocalOntologyFileName();
            File currentFile  = new File(currentFileName);
            Path currentFilePath = currentFile.toPath();
            String parentFolderLocation = currentFile.getParentFile().getAbsolutePath();
            File parentFolder = new File(parentFolderLocation);
            boolean exists = parentFolder.exists();
            if(!exists)
            {
                MessageDialog.openWarning(this.getContainer().getShell(), "Missing Folder", 
                        "The parent folder for the ontology could not be found. "
                        + "A new folder will be created.");
                exists = parentFolder.mkdirs();
            }
            exists = currentFile.exists() ? true : currentFile.createNewFile();
            if(exists)
            {
                File backUpFile = new File(parentFolderLocation 
                        + File.separator 
                        + "Backup_Ontology.owl");
                OutputStream backupOutputStream = new FileOutputStream(backUpFile);
                Files.copy(currentFilePath, backupOutputStream);
                backupOutputStream.close();
                try
                {
                    FileWriter fileWriter = new FileWriter(currentFileName);
                    this.localOntologyModel.write(fileWriter, "RDF/XML-ABBREV");
                    fileWriter.close();
                    backUpFile.delete();

                } catch (Exception ex)
                {
                    logger.error("Error copying current model to ontology file");
                    try
                    {
                        FileOutputStream os = new FileOutputStream(currentFile);
                        Files.copy(backUpFile.toPath(), os);
                        os.close();
                        backUpFile.delete();
                    } catch (IOException e)
                    {
                        logger.error("Error while reloading backup ontology file.", ex);
                        throw ex;
                    }
                }
            }
            else{
                logger.fatal("Ontology File " + currentFileName + " could not be found or created");
                MessageDialog.openError(this.getActivePageInstance().getManagedForm().getForm().getShell(), 
                        "Error Saving Ontology", "The ontology file could not be saved.");
            }

        } catch (Exception ex)  {
            logger.error("Error synchronizing model with ontology file.", ex);
            throw ex;
        }
        logger.debug("- END   : Synchronizing ontology file with model.");
    }

    @Override
    protected void addPages()
    {
        logger.debug("- START : Retrieving lists and Creating pages for the Manager Editor.");

        try
        {
            try
            {
                addPage(0, new DesciptorManagementPage(this));
                addPage(1, new DescriptorGroupManagementPage(this));
                addPage(2, new TemplateManagementPage(this));
                addPage(3, new CategoryManagementPage(this));
            } catch (PartInitException e)
            {
                logger.error("Error adding pages", e);
            }
        } catch (Exception ex)  
        {
            logger.error("Error creating pages.", ex);
            throw ex;
        }
        logger.debug("- END   : Retrieving lists and Creating pages for the Manager Editor.");
    }

    protected void pageChange(int newPageIndex) {

        logger.debug("- START : Reloading while changing to page " + newPageIndex);

        try
        {
            super.pageChange(newPageIndex);
            ((AbstractManagementPage) this.getActivePageInstance()).refreshPage();
        } catch (Exception ex)  {
            logger.error(ex);
        }

        logger.debug("- END   : Reloading while changing to page " + newPageIndex);
    }

    @Override
    public void doSave(IProgressMonitor monitor)
    {
        logger.debug("- START : Saving model.");

        try
        {
            String warnMessage = ModelValidator.validateModel(this.ontologyManagerApi, this.localOntologyModel);
            if(warnMessage != null)
            {

                MessageDialog.openWarning(this.getActivePageInstance().getManagedForm()
                        .getForm().getShell(), "Model Warning", 
                        "Warning : The changes are being saved with following potential problem(s) : \n\n"
                        + warnMessage);
            }
            this.synchronizingFileWithModel();
            this.resetDirtyFlag();
        } catch (Exception ex)  {
            logger.error("Error saving Model.", ex);
            MessageDialog.openError(this.getActivePageInstance().getManagedForm()
                    .getForm().getShell(), "Error In Saving", 
                    "The changes could not be saved to the file.");
        }

        logger.debug("- END   : Saving model.");
    }

    private void resetDirtyFlag()
    {
        this.dirtyFlag = false;
        firePropertyChange(FormEditor.PROP_DIRTY);
    }

    @Override
    public void doSaveAs()
    {

    }

    @Override
    public boolean isSaveAsAllowed()
    {
        return false;
    }


    public boolean isDirty()
    {
        return this.dirtyFlag ;
    }

    public void markDirty() 
    {
        this.dirtyFlag = true;
        super.firePropertyChange(FormEditor.PROP_DIRTY);
    }

    public HashMap<String, String> getAllCategoriesURILabelMap()
    {
        if(managerMode)
            return this.ontologyManagerApi.getAllCategoriesURILabelMap(localOntologyModel);
        else
            return this.ontologyManagerApi.getAllCategoriesURILabelMap(standardOntologyModel);
    }

    public List<String> getAllIndividualLabels(String classUri)
    {
        try
        {
            logger.debug("- START : Retrieving all labels for the class " + classUri);
            List<String> allIndiviualLabels = new ArrayList<String>();
            OntClass classResource = this.localOntologyModel.getOntClass(classUri);
            Set<Resource> results = this.localOntologyModel.listSubjectsWithProperty(RDF.type, 
                    classResource).toSet();
            Individual indiv;
            for(Resource result : results)
            {
                indiv = this.localOntologyModel.getIndividual(result.getURI());
                allIndiviualLabels.add(indiv.getLabel(null));
            }
            if(!managerMode)
            {
                results = this.standardOntologyModel.listSubjectsWithProperty(RDF.type, 
                        classResource).toSet();
                for(Resource result : results)
                {
                    indiv = this.standardOntologyModel.getIndividual(result.getURI());
                    allIndiviualLabels.add(indiv.getLabel(null));
                }
            }

            logger.debug("- END   : Retrieving all labels for the class " + classUri);

            return allIndiviualLabels;
        } catch (Exception ex)  {
            logger.error(ex);
            throw ex;
        }
    }

}
