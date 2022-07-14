/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.ontology;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;
import org.grits.toolbox.entry.sample.ontologymanager.SampleOntologyApi;
import org.grits.toolbox.entry.sample.ontologymanager.SampleOntologyManager;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;
import com.hp.hpl.jena.shared.AlreadyExistsException;
import com.hp.hpl.jena.shared.NotFoundException;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * This class manages sample ontology by editing information stored in it. 
 * It extends the {@link SampleOntologyApi} class from sample plugin and thus uses all its
 * methods available for reading information from the two ontologies. It provides
 * api for all the major functionalities of adding/removing information to/from the ontology.
 * It has two modes of operation:
 * <p>
 * <b>USER_MODE</b> - This mode allows modifying information in local ontology, while giving a
 * read access to standard ontology.
 * <p>
 * <b>ADMIN_MODE</b> - This mode displays and allows modification of information in the standard ontology.
 * In this mode no information from local ontology is visible.
 * 
 *
 */
public class SampleOntologyEditApi extends SampleOntologyApi implements ISampleOntologyEditApi
{
	private Logger logger = Logger.getLogger(SampleOntologyEditApi.class);
	protected EditMode editMode = null;

	public SampleOntologyEditApi(EditMode editMode) throws Exception
	{
		super();
		logger.info("initializing sample manager in mode : " + editMode);
		editMode = editMode == null ? EditMode.USER_MODE : editMode;
		try
		{
			switch (editMode)
			{
				case USER_MODE:
					logger.info("loading local sample ontology in USER mode");
					// initialize it as usual with two ontology model api
					break;

				case ADMIN_MODE:
					logger.info("setting standard ontology as local ontology in ADMIN mode");
					// only standard ontology is used for all the queries in this mode, so
					// set standard ontology file and model as its local ontology file and model
					super.localOntologyFile = super.standardOntologyFile;
					super.localOntModel = getStandardOntModel();
					break;

				default:
					logger.error("This edit mode is not supported : " + editMode);
					throw new Exception("This edit mode is not supported : " + editMode);
			}
		} catch (IOException ex)
		{
			logger.error("error loading ontologies\n" + ex.getMessage(), ex);
			throw ex;
		} catch (Exception ex)
		{
			logger.fatal("something unexpected went wrong while loading ontologies\n" + ex.getMessage(), ex);
			throw ex;
		}

		logger.info("sample ontologies loaded");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createNewIndividual(String classUri, String label) throws AlreadyExistsException
	{
		logger.info("creating new individual for class " + classUri + " with label " + label);

		// both of the parameters are not nullable
		if(classUri == null || label == null)
			return null;

		// check if there exists a class with this uri
		OntResource classResource = localOntModel.getOntClass(classUri);
		if(classResource == null)
		{
			logger.info("no class found for this uri : " + classUri);
			return null;
		}

		// check for class label as it is needed for generating uri
		String classLabel = classResource.getLabel(null);
		if(classLabel == null)
		{
			logger.error("no label found for found class " + classUri);
			return null;
		}

		// make a modified label to generate uri
		// concatenate them with " "
		String modifiedLabel = classLabel + " " + label;

		// create a uri from this modified label
		String indivUri = getUriForLabel(modifiedLabel);
		if(localOntModel.getOntResource(indivUri) != null)
		{
			logger.error("some individual with this class_individual combination already exists"
					+ " in this ontology : " + modifiedLabel + "\nuri : " + indivUri);
			throw new AlreadyExistsException(indivUri);
		}

		Individual indiv = localOntModel.createIndividual(indivUri, classResource);
		indiv.setLabel(label, null);

		// return the uri of the new individual
		logger.info("individual created : " + indivUri);
		return indivUri;
	}

	private String getUriForLabel(String givenLabel)
	{
		return SampleOntologyManager.baseURI + givenLabel.toLowerCase().trim()
					.replaceAll(" ", "_").replaceAll("/", "_").replaceAll("%", "_");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getLocalIndividualURIs(String classUri)
	{
		logger.debug("getting local uris for class : " + classUri);

		if(classUri == null || localOntModel.getOntClass(classUri) == null)
			return null;

		Set<String> localUris = new HashSet<String>();

		final OntClass ontClass = localOntModel.getOntClass(classUri);
		for(OntResource instance : localOntModel.listIndividuals(
				localOntModel.getOntClass(classUri)).filterKeep(new Filter<Individual>()
				{
					@Override
					public boolean accept(Individual indiv)
					{
						// add individuals whose class statement is defined in local model
						logger.debug(indiv.getURI().toUpperCase());
						boolean accept = localOntModel.isInBaseModel(
								new StatementImpl(indiv, RDF.type, ontClass));
						logger.debug("added : " + accept);
						return accept;
					}
				}).toSet())
		{
			localUris.add(instance.getURI());
		}

		return localUris;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateLabel(String uri, String label) throws NotFoundException
	{
		logger.info("updating label for : " + uri + " with new label : " + label);
		if(uri == null || label == null)
			return false;

		Individual localIndiv = getDefinedIndivFromLocalOntology(uri);
		localIndiv.setLabel(label, null);
		logger.info("label updated for " + localIndiv.getURI() +
				" with new label " + localIndiv.getLabel(null));
		return true;
	}

	/**
	 * returns an individual that has a label and a class in the local ontology
	 * @param uri the uri of the individual to be found
	 * @return an individual or null if uri was null
	 * @throws NotFoundException if the uri was not found in the ontology or the 
	 * indiviudal found did not have any label or any class (a class should have a uri).
	 */
	private Individual getDefinedIndivFromLocalOntology(String uri) throws NotFoundException
	{
		if(uri == null)
			return null;

		Individual localIndiv = localOntModel.getIndividual(uri);
		if(localIndiv == null) // individual not found in the ontology
		{
			logger.error("individual to be updated not found in current ontology");
			throw new NotFoundException("individual to be updated not found in current ontology");
		}

		// it has to be defined in the local ontology with a label and class
		OntClass indivClass = localIndiv.getOntClass(true);
		if(localIndiv.getLabel(null) == null ||
				indivClass == null || indivClass.getURI() == null)
		{
			logger.error("indiv to be updated not defined in current ontology");
			throw new NotFoundException("indiv to be updated not defined in current ontology");
		}

		logger.debug("exists with name : " + localIndiv.getLabel(null) +
				" in class : " + indivClass.getURI());
		return localIndiv;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateDescription(String uri, String description) throws NotFoundException
	{
		logger.info("updating description for : " + uri + " with new description : " + description);
		if(uri == null)
			return false;

		Individual localIndiv = getDefinedIndivFromLocalOntology(uri);
		// delete previous descriptions since "addComment" method does not remove previous comments
		localIndiv.removeAll(RDFS.comment);
		localIndiv.addComment(description, null);
		logger.info("description updated for " + localIndiv.getURI() +
				" with new description " + localIndiv.getComment(null));
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateCategorySelection(String categoryUri, String descriptorOrGroupUri, boolean selected)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addUnitToDescriptor(String descriptorUri, MeasurementUnit measurementUnit)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setDefaultUnitForDescriptor(String descriptorUri, String unitUri)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addNamespaceToDescriptor(String descriptorUri, String namespaceUri)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addDescriptorToGroup(String groupUri, String descriptorUri, Integer maxOcurrence, boolean mandatory)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addDescriptorOrGroupToCategoryTemplate(String templateUri, String categoryUri, String descriptorUri,
			Integer maxOcurrence, boolean mandatory)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setMaxOccurrence(String descriptorOrGroupUri, Integer maxOccurrence)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setMaxOccurrenceInGroup(String groupUri, String descriptorUri, Integer maxOccurrence)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setMaxOccurrenceInTemplate(String templateUri, String categoryUri, String descriptorOrGroupUri,
			Integer maxOccurrence)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setMandatoryDescriptorInGroup(String groupUri, String descriptorUri, boolean mandatory)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setMandatoryDescriptorInCategoryTemplate(String templateUri, String categoryUri,
			String descriptorUri, boolean mandatory)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeDescriptorFromGroup(String groupUri, String descriptorUri)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeDescriptorOrGroupFromTemplate(String templateUri, String categoryUri, String descriptorUri)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeIndividual(String indivUri)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeTriple(String subjectUri, String propertyUri, String objectUri)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MeasurementUnit> getAllMeasurementUnits()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean saveModel()
	{
		try
		{
			super.saveLocalOntology();
			return true;
		} catch (Exception ex)
		{
			logger.fatal(ex.getMessage(), ex);
		}
		return false;
	}
}
