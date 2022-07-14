/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.ontology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.grits.toolbox.entry.sample.model.Category;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;
import org.grits.toolbox.entry.sample.model.Namespace;
import org.grits.toolbox.entry.sample.model.Template;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * 
 *
 */
public class GritsOntologyManagerApi
{
	private static final Logger logger = Logger.getLogger(GritsOntologyManagerApi.class);

	private static final String NAMESPACE_CLASS_URI = OntologyManager.baseURI + "namespace";
	private static final String MEASUREMENT_UNIT_CLASS_URI = OntologyManager.baseURI + "unit";
	private OntologyManager ontologyManager = null;
	private OntModel defaultOntologyModel = null;
	private OntModel readOnlyOntologyModel = null;
	private boolean managerMode = false;

	public GritsOntologyManagerApi(OntModel localOntologyModel)
	{
		ontologyManager = new OntologyManager(localOntologyModel);
		defaultOntologyModel  = localOntologyModel;
		managerMode  = true;
	}

	public GritsOntologyManagerApi(OntModel standardOntologyModel, OntModel localOntologyModel)
	{
		ontologyManager = new OntologyManager(standardOntologyModel, localOntologyModel);
		readOnlyOntologyModel = standardOntologyModel;
		defaultOntologyModel  = localOntologyModel;
	}

	/**
	 * get all the descriptors from the ontology
	 * @param model ontology to look
	 * @return list of descriptors
	 */
	public List<Descriptor> getAllDescriptors(OntModel model)
	{
		logger.debug("- START : Retrieving all Descriptors from the ontology model.");
		List<Descriptor> descriptors = new ArrayList<Descriptor>();
		try
		{
			String uri = null;
			OntClass descriptorClass= model.getOntClass(OntologyManager.DESCRIPTOR_CLASS_URI);
			ExtendedIterator<Individual> indivs = model.listIndividuals(descriptorClass);
			while(indivs.hasNext()) {
				Individual indiv = indivs.next();
				uri = indiv.getURI();
				descriptors.add(ontologyManager.getDescriptorByUri(model, uri));
			}
		} catch (Exception ex)
		{
			//ex.printStackTrace();
			logger.error(ex);
		}
		logger.debug("- END   : Retrieving all Descriptors from the ontology model.");
		return descriptors;
	}

	/**
	 * get all descriptor groups from the ontology
	 * @param model ontology to look
	 * @return list of descriptor groups
	 */
	public List<DescriptorGroup> getAllDescriptorGroups(OntModel model)
	{
		logger.debug("- START : Retrieving all DescriptorGroups from the ontology model.");
		List<DescriptorGroup> descriptorGroups = new ArrayList<DescriptorGroup>();
		try
		{
			List<Individual> indivs = ontologyManager.getAllDescriptorGroup(model);
			for(Individual indiv : indivs) {
				descriptorGroups.add(ontologyManager.getDescriptorGroupByUri(model, indiv.getURI()));
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Retrieving all DescriptorGroups from the ontology model.");
		return descriptorGroups;
	}

	public void updateLabel(String uri, String label)
	{
		logger.debug("- START : Updating label for " + uri + " as " + label + " in the ontology model.");
		try
		{
			Resource resource = this.defaultOntologyModel.getResource(uri);
			if(resource != null)
			{
				resource.removeAll(RDFS.label);
				this.defaultOntologyModel.add(resource, RDFS.label, label);
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Updating label for " + uri + " as " + label + " in the ontology model.");
	}

	public void updateDescription(String uri, String label)
	{
		logger.debug("- START : Updating description for " + uri + " as " + label + " in the ontology model.");
		try
		{
			Resource resource = this.defaultOntologyModel.getResource(uri);
			if(resource != null)
			{
				resource.removeAll(RDFS.comment);
				this.defaultOntologyModel.add(resource, RDFS.comment, label);
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Updating description for " + uri + " as " + label + " in the ontology model.");
	}

	public void updateMaxOccurrence(String uri, Integer maxOccurrence)
	{
		logger.debug("- START : Updating Max Occurrence for " + uri + " as " + maxOccurrence + " in the ontology model.");
		try
		{
			Resource resource = this.defaultOntologyModel.getResource(uri);
			if(resource != null)
			{
				DatatypeProperty property = this.defaultOntologyModel.getDatatypeProperty(
						OntologyManager.baseURI + "has_abbundance");
				resource.removeAll(property);
				if(maxOccurrence != null)
					this.defaultOntologyModel.addLiteral(resource, property, maxOccurrence.intValue());
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Updating Max Occurrence for " + uri + " as " + maxOccurrence + " in the ontology model.");
	}
	
	public void updatePosition(String uri, Integer position)
	{
		logger.debug("- START : Updating Position for " + uri + " as " + position + " in the ontology model.");
		try
		{
			Resource resource = this.defaultOntologyModel.getResource(uri);
			if(resource != null)
			{
				DatatypeProperty property = this.defaultOntologyModel.getDatatypeProperty(
						OntologyManager.baseURI + "has_position");
				resource.removeAll(property);
				if(position != null)
					this.defaultOntologyModel.addLiteral(resource, property, position.intValue());
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Updating Position for " + uri + " as " + position + " in the ontology model.");
	}
	
	public void updateStandardGuideline(String uri, String mirageURI)
	{
		logger.debug("- START : Updating StandardGuideline for " + uri + " as " + mirageURI + " in the ontology model.");
		
		try
		{
			Resource resource = this.defaultOntologyModel.getResource(uri);
			String guidelineUri = OntologyManager.GUIDELINE_CLASS + "_" + mirageURI.toLowerCase().replaceAll(" ", "_");
			Individual guideline = this.defaultOntologyModel.getIndividual(guidelineUri);
			if (guideline == null) {
				// create it for the first time
				guideline = this.defaultOntologyModel.createIndividual(guidelineUri, this.defaultOntologyModel.getOntClass(OntologyManager.GUIDELINE_CLASS));
				guideline.setLabel(mirageURI, null);
			}
			if(resource != null && guideline != null)
			{
				ObjectProperty property = this.defaultOntologyModel.getObjectProperty(OntologyManager.baseURI + "guideline_info");
				if(!this.defaultOntologyModel.contains(resource, property, guideline))
					resource.addProperty(property, guideline);
			}
		} catch (Exception ex)
		{
			logger.error("Error adding guideline_info", ex);
		}
		
		logger.debug("- END   : Updating Mirage for " + uri + " as " + mirageURI + " in the ontology model.");
	}

	/**
	 * create a map containing category URIs to their corresponding labels in the ontology
	 * @param model
	 * @return a map with keys containing category URIs and values are the correspoding labels for the categories
	 */
	public HashMap<String, String> getAllCategoriesURILabelMap(OntModel model)
	{
		try
		{
			logger.debug("- START : Retrieving all Categories Label from the ontology model.");

			HashMap<String, String> categoryURIs = new HashMap<String, String>();
			List<Individual> indivs = this.ontologyManager.getAllIndiviudalsOfClass(model, 
					OntologyManager.CATEGORY_CLASS_URI);
			for(Individual indiv : indivs)
			{
				categoryURIs.put(indiv.getURI(), indiv.getLabel(null));
			}

			logger.debug("- END   : Retrieving all Categories Label from the ontology model.");

			return categoryURIs;
		} catch (Exception ex)
		{
			logger.error(ex);
			throw ex;
		}
	}

	/**
	 * get all categories from the ontology
	 * @return list of categories
	 * @throws Exception
	 */
	public List<Category> getAllCategories() throws Exception
	{
		try
		{
			logger.debug("- START : Retrieving all Categories from the ontology model.");

			List<Category> categories = new ArrayList<Category>();
			List<Individual> indivs = new ArrayList<Individual>();
			if(!managerMode)
				indivs = this.ontologyManager.getAllIndiviudalsOfClass(readOnlyOntologyModel, OntologyManager.CATEGORY_CLASS_URI);
			else
				indivs =this.ontologyManager.getAllIndiviudalsOfClass(defaultOntologyModel, OntologyManager.CATEGORY_CLASS_URI);
			Category category = null;
			Property categoryProperty = this.defaultOntologyModel.getProperty(OntologyManager.baseURI + "has_category");
			Set<Resource> subjectsWithCategory = new HashSet<Resource>(); 

			Individual subjectIndiv = null;
			OntClass subjectClass = null;
			String subjectClassURI = null;
			String subjectURI = null;
			Descriptor descriptor = null;
			DescriptorGroup descriptorGroup = null;
			String indivURI = null;
			try {
				for(Individual indiv : indivs)
				{
					indivURI = indiv.getURI();
					category = new Category(indiv.getURI());

					subjectsWithCategory = this.defaultOntologyModel.listSubjectsWithProperty(categoryProperty, 
							this.defaultOntologyModel.getResource(indivURI)).toSet();
					if(!managerMode)
						subjectsWithCategory.addAll(this.readOnlyOntologyModel.listSubjectsWithProperty(categoryProperty, 
								this.readOnlyOntologyModel.getResource(indivURI)).toSet());
					for(Resource subject : subjectsWithCategory)
					{
						subjectURI = subject.getURI();
						subjectIndiv = this.getIndividual(subjectURI);
						if(subjectIndiv != null)
						{
							subjectClass = subjectIndiv.getOntClass();
							subjectClassURI = subjectClass.getURI();
							switch (subjectClassURI)
							{
							case OntologyManager.DESCRIPTOR_CLASS_URI :
								descriptor = null;
								if(!managerMode)
								{
									descriptor = ontologyManager.getDescriptorByUri(readOnlyOntologyModel, subjectURI);
								}
								if(descriptor == null)
									descriptor = ontologyManager.getDescriptorByUri(defaultOntologyModel, subjectURI);
								if(descriptor != null)
									category.addDescriptor(descriptor);
								break;
							case OntologyManager.DESCRIPTOR_GROUP_CLASS_URI :
								descriptorGroup = null;
								if(!managerMode)
								{
									descriptorGroup = ontologyManager.getDescriptorGroupByUri(readOnlyOntologyModel, subjectURI);
								}
								if(descriptorGroup == null)
									descriptorGroup = ontologyManager.getDescriptorGroupByUri(defaultOntologyModel, subjectURI);
								if(descriptorGroup != null)
									category.addDescriptorGroup(descriptorGroup);
							}
						}
					}
					categories.add(category);
				}
				//                Collections.sort(categories, new CategoryURIComparator());
			} catch (Exception ex)
			{
				logger.error(ex);
				throw ex;
			}

			logger.debug("- END   : Retrieving all Categories from the ontology model.");

			return categories ;
		} catch (Exception ex)
		{
			logger.error(ex);
			throw ex;
		}
	}

	public void updateCategorySelection(String categoryUri, String descriptorTypeUri, boolean selected)
	{
		logger.debug("- START : Updating Category " + categoryUri + " for " 
				+ descriptorTypeUri + " as selection " + selected + " in the ontology model.");
		try
		{
			Resource descriptorType = this.defaultOntologyModel.getResource(descriptorTypeUri);
			Individual category = this.defaultOntologyModel.getIndividual(categoryUri);
			if(descriptorType != null && category != null)
			{
				ObjectProperty property = this.defaultOntologyModel.getObjectProperty(OntologyManager.baseURI + "has_category");
				if(selected)
				{
					if(!this.defaultOntologyModel.contains(descriptorType, property, category))
						descriptorType.addProperty(property, category);
				}
				else
				{
					this.defaultOntologyModel.remove(descriptorType, property, category);
				}
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Updating Category " + categoryUri + " for " 
				+ descriptorTypeUri + " as selection " + selected + " in the ontology model.");
	}

	/**
	 * get all existing measurement units from the ontology
	 * @param model ontology to look for
	 * @return list of measurement units
	 */
	public List<MeasurementUnit> getAllMeasurementUnits(OntModel model)
	{
		logger.debug("- START : Retrieving all Measurement Units from the ontology model.");
		List<MeasurementUnit> allUnits = new ArrayList<MeasurementUnit>();
		try
		{
			List<Individual> unitIndivs = this.ontologyManager.getAllIndiviudalsOfClass(model, GritsOntologyManagerApi.MEASUREMENT_UNIT_CLASS_URI);
			for(Individual indiv : unitIndivs)
			{
				allUnits.add(new MeasurementUnit(indiv.getURI(), indiv.getLabel(null)));
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Retrieving all Measurement Units from the ontology model.");
		return allUnits;
	}

	/**
	 * adds a measurement unit to an existing descriptor
	 * 
	 * descriptor has to be there in the local ontology
	 * unit might not be there in any of the ontologies (the method will create it)
	 * @param descriptorURI
	 * @param measurementUnit
	 */
	public void addUnitToDescriptor(String descriptorURI, MeasurementUnit measurementUnit)
	{
		logger.debug("- START : Adding a Measurement Unit " + measurementUnit + 
				" to Descriptor " + descriptorURI + " in the ontology model.");
		try
		{
			Individual unitIndiv = this.defaultOntologyModel.getIndividual(measurementUnit.getUri());
			if(unitIndiv == null && !managerMode)
			{
				unitIndiv = this.readOnlyOntologyModel.getIndividual(measurementUnit.getUri());
				this.defaultOntologyModel.createIndividual(unitIndiv);
			}
			if(unitIndiv == null)
			{
				Resource mUnit = this.defaultOntologyModel.createResource(measurementUnit.getUri(), 
						this.defaultOntologyModel.getOntClass(GritsOntologyManagerApi.MEASUREMENT_UNIT_CLASS_URI));
				this.defaultOntologyModel.add(mUnit, RDFS.label, measurementUnit.getLabel());
			}
			this.addObjectPropertyStatement(descriptorURI, "has_unit_of_measurement", measurementUnit.getUri());
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Adding a Measurement Unit " + measurementUnit + 
				" to Descriptor " + descriptorURI + " in the ontology model.");
	}

	/**
	 * descriptor has to be there in the local ontology
	 * unit has to be there in any of the ontologies
	 * it only links the unit with the descriptor
	 * @param descriptorURI
	 * @param measurementUnit
	 */
	public void setDefaultUnitForDescriptor(String descriptorURI, String unitUri)
	{
		logger.debug("- START : Setting default measurement unit " + unitUri + 
				" for Descriptor " + descriptorURI + " in the ontology model.");
		try
		{
			Individual descriptorIndiv = null;
			Property defaultUnitProperty = null;
			if(managerMode)
			{
				descriptorIndiv = this.defaultOntologyModel.getIndividual(descriptorURI);
				defaultUnitProperty = this.defaultOntologyModel.getProperty(OntologyManager.baseURI + "has_default_unit_of_measurement");
				descriptorIndiv.removeAll(defaultUnitProperty);
			}
			else 
			{
				descriptorIndiv = this.readOnlyOntologyModel.getIndividual(descriptorURI);
				if(descriptorIndiv == null)
				{
					descriptorIndiv = this.defaultOntologyModel.getIndividual(descriptorURI);
					defaultUnitProperty = this.defaultOntologyModel.getProperty(OntologyManager.baseURI + "has_default_unit_of_measurement");
					descriptorIndiv.removeAll(defaultUnitProperty);
				}
				else
				{
					defaultUnitProperty = this.readOnlyOntologyModel.getProperty(OntologyManager.baseURI + "has_default_unit_of_measurement");
					descriptorIndiv.removeAll(defaultUnitProperty);
				}
			}
			Individual unitIndiv = this.getIndividual(unitUri);
			if(descriptorIndiv != null && unitIndiv != null)
			{
				this.addObjectPropertyStatement(descriptorURI, "has_default_unit_of_measurement", unitUri);
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Setting default measurement unit " + unitUri + 
				" for Descriptor " + descriptorURI + " in the ontology model.");
	}

	private void addObjectPropertyStatement(String subject,
			String property, String object)
	{
		Resource subjectResource = this.defaultOntologyModel.getResource(subject);
		Individual objectIndiv = this.defaultOntologyModel.getIndividual(object);
		if(subjectResource != null && objectIndiv != null)
		{
			ObjectProperty objectProperty = this.defaultOntologyModel.getObjectProperty(OntologyManager.baseURI + property);
			subjectResource.addProperty(objectProperty, objectIndiv);
		}
	}

	public void removeTriple(String subjectURI, String propertyURI,
			String objectURI)
	{
		Resource subjectResource = this.defaultOntologyModel.getResource(subjectURI);
		Individual objectIndiv = this.defaultOntologyModel.getIndividual(objectURI);
		if(subjectResource != null && objectIndiv != null)
		{
			ObjectProperty objectProperty = this.defaultOntologyModel.getObjectProperty(OntologyManager.baseURI + propertyURI);
			this.defaultOntologyModel.remove(subjectResource, objectProperty, objectIndiv);
		}
	}


	public List<Template> getAllTemplates(OntModel model)
	{
		logger.debug("- START : Retrieving all Templates from the ontology model.");

		List<Template> templates = new ArrayList<Template>();
		try
		{
			List<Individual> templateIndivs = this.ontologyManager.getAllTemplates(model);
			for(Individual tempIndiv : templateIndivs)
			{
				templates.add(this.ontologyManager.getTemplate(model, tempIndiv.getURI()));
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}

		logger.debug("- END   : Retrieving all Templates from the ontology model.");
		return templates;
	}


	public void removeDescriptorFromGroup(String groupURI, String descriptorURI)
	{
		logger.debug("- START : Removing Descriptor " + descriptorURI + 
				" from Descriptor Group " + groupURI + " in the ontology model.");
		try
		{
			Individual groupIndiv = this.defaultOntologyModel.getIndividual(groupURI);
			Property property = this.getProperty("has_descriptor_group_context");
			Set<RDFNode> dgContexts = this.defaultOntologyModel.listObjectsOfProperty(groupIndiv, property).toSet();
			Individual descriptorIndiv = this.getIndividual(descriptorURI);
			property = this.getProperty("has_descriptor");
			Resource dgContextToBeRemoved = null;
			for(RDFNode dgContext : dgContexts)
			{
				if(dgContext.asResource().getPropertyResourceValue(property).getURI().equals(descriptorIndiv.getURI()))
				{
					dgContextToBeRemoved = dgContext.asResource();
					break;
				}
			}
			this.defaultOntologyModel.removeAll(null, null, dgContextToBeRemoved);
			this.defaultOntologyModel.removeAll(dgContextToBeRemoved, null, null);
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Removing Descriptor " + descriptorURI + 
				" from Descriptor Group " + groupURI + " in the ontology model.");
	}

	private Resource getResource(String givenURI)
	{
		Resource resource = null;
		if(managerMode)
		{
			resource = this.defaultOntologyModel.getIndividual(givenURI);
		}
		else 
		{
			resource = this.readOnlyOntologyModel.getIndividual(givenURI);
			if(resource == null)
			{
				resource = this.defaultOntologyModel.getIndividual(givenURI);
			}
		}
		return resource;
	}

	private Individual getIndividual(String givenURI)
	{
		Individual indiv = null;
		if(managerMode)
		{
			indiv = this.defaultOntologyModel.getIndividual(givenURI);
		}
		else 
		{
			indiv = this.readOnlyOntologyModel.getIndividual(givenURI);
			if(indiv == null)
			{
				indiv = this.defaultOntologyModel.getIndividual(givenURI);
			}
		}
		//        Individual indiv = this.defaultOntologyModel.getIndividual(givenURI);
		return indiv;
	}

	private Property getProperty(String givenSuffixURI)
	{
		Property property = null;
		if(managerMode)
		{
			property = this.defaultOntologyModel.getProperty(OntologyManager.baseURI + givenSuffixURI);
		}
		else 
		{
			property = this.readOnlyOntologyModel.getProperty(OntologyManager.baseURI + givenSuffixURI);
			if(property == null)
			{
				property = this.defaultOntologyModel.getProperty(OntologyManager.baseURI + givenSuffixURI);
			}
		}
		return property;
	}

	private String createNewIndividualURI(String className)
	{
		try
		{
			logger.debug("- START : Creating a new Individual URI for class " + className + " in the ontology model.");
			String newURI = OntologyManager.baseURI + className.toLowerCase().replaceAll(" ", "_");
			Random random = new Random();
			int randomLength = 8;
			boolean notUnique = true;
			String randomSuffix = null;
			char randamCharacter;
			Resource searchResource = null;
			while(notUnique)
			{
				randomSuffix = "";
				for(int i = 0; i< randomLength; i++)
				{
					randamCharacter = (char) (97 + random.nextInt(26));
					randomSuffix = randomSuffix + randamCharacter;
				}
				searchResource = ResourceFactory.createResource(newURI + "_" + randomSuffix);
				notUnique = defaultOntologyModel.containsResource(searchResource);
				if(readOnlyOntologyModel != null)
				{
					notUnique  = notUnique || readOnlyOntologyModel.containsResource(searchResource);
				}
			}

			newURI = searchResource.getURI();

			logger.debug("- END   : Creating a new Individual URI for class " + className + " in the ontology model.");

			return newURI;

		} catch (Exception ex)
		{
			logger.error(ex);
			throw ex;
		}
	}

	/**
	 * add a descriptor to a descriptor group
	 * 
	 * @param groupURI URI of the descriptor group (should already be in the ontology)
	 * @param descriptorURI URI of the descriptor (should already be in the ontology)
	 * @param maxOcurrence multiplicity of the descriptor within the group
	 * @param mandatory whether the descriptor is mandatory or not
	 */
	public void addDescriptorToGroup(String groupURI, String descriptorURI,
			Integer maxOcurrence, boolean mandatory)
	{
		logger.debug("- START : Adding Descriptor " + descriptorURI + 
				" to Descriptor Group " + groupURI + " in the ontology model.");
		try
		{
			String dgContextURI = this.createNewIndividualURI("Descriptor Group Context");
			Individual dgContextIndiv = this.defaultOntologyModel.createIndividual(dgContextURI,
					this.defaultOntologyModel.getOntClass(OntologyManager.baseURI+ "descriptor_group_context"));
			dgContextIndiv.setLabel(dgContextURI.substring(OntologyManager.baseURI.length()), null);
			Property property = this.getProperty("is_mandatory");
			dgContextIndiv.addLiteral(property, mandatory);

			if(maxOcurrence != null)
			{
				property = this.getProperty("has_abbundance");
				dgContextIndiv.addLiteral(property, maxOcurrence);
			}
			Individual descriptorIndiv = this.getIndividual(descriptorURI);

			property = this.getProperty("has_descriptor");
			this.defaultOntologyModel.add(dgContextIndiv, property, descriptorIndiv);
			Resource groupResource = this.getResource(groupURI);
			property = this.getProperty("has_descriptor_group_context");
			this.defaultOntologyModel.add(groupResource, property, dgContextIndiv);
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Adding Descriptor " + descriptorURI + 
				" to Descriptor Group " + groupURI + " in the ontology model.");
	}

	/**
	 * change/set the mandatory state for a given descriptor in a given group
	 * @param groupURI URI of an existing descriptor group
	 * @param descriptorURI URI of the descriptor to be modified
	 * @param mandatory new value for the mandatory state
	 */
	public void setMandatoryStateDescriptorInGroup(String groupURI,
			String descriptorURI, boolean mandatory)
	{
		logger.debug("- START : Setting Descriptor " + descriptorURI + 
				" as mandatory value " + mandatory + " for Descriptor Group " + groupURI + " in the ontology model.");
		try
		{
			Individual groupIndiv = this.defaultOntologyModel.getIndividual(groupURI);
			Property property = this.getProperty("has_descriptor_group_context");
			Set<RDFNode> dgContexts = this.defaultOntologyModel.listObjectsOfProperty(groupIndiv, property).toSet();
			Individual descriptorIndiv = this.getIndividual(descriptorURI);
			property = this.getProperty("has_descriptor");
			Resource dgContextToBeUpdated = null;
			for(RDFNode dgContext : dgContexts)
			{
				if(dgContext.asResource().getPropertyResourceValue(property).getURI().equals(descriptorIndiv.getURI()))
				{
					dgContextToBeUpdated = dgContext.asResource();
					break;
				}
			}
			property = this.getProperty("is_mandatory");
			//        Resource resource = dgContextToBeUpdated.getPropertyResourceValue(property);
			//        Literal previousLiteralValue = resource.asLiteral();
			//        boolean previousValue = previousLiteralValue.getBoolean();
			this.defaultOntologyModel.removeAll(dgContextToBeUpdated, property, null);
			dgContextToBeUpdated.addLiteral(property, mandatory);
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Setting Descriptor " + descriptorURI + 
				" as mandatory value " + mandatory + " for Descriptor Group " + groupURI + " in the ontology model.");
	}


	public void setMandatoryStateDescriptorInCategoryTemplate(String templateURI, String categoryURI, 
			String descriptorURI, boolean mandatory)
	{
		logger.debug("- START : Setting Descriptor / Descriptor Group " + descriptorURI 
				+ " as mandatory value " + mandatory 
				+ " in category " + categoryURI 
				+ " for Template " + templateURI + " in the ontology model.");
		try
		{
			Individual templateIndiv = this.defaultOntologyModel.getIndividual(templateURI);
			Property property = this.getProperty("has_template_context");
			Set<RDFNode> templateContexts = this.defaultOntologyModel.listObjectsOfProperty(templateIndiv, property).toSet();
			Individual descriptorIndiv = this.getIndividual(descriptorURI);
			property = this.getProperty("has_template_descriptor");
			Property categoryProperty = this.getProperty("has_category");
			Resource templateContextToBeUpdated = null;
			for(RDFNode tempContext : templateContexts)
			{
				if(tempContext.asResource().getPropertyResourceValue(property).getURI().equals(descriptorIndiv.getURI()))
				{
					Individual templateContextIndiv = this.defaultOntologyModel.getIndividual(tempContext.asResource().getURI());
					Set<RDFNode> categoryResources = this.defaultOntologyModel.listObjectsOfProperty(templateContextIndiv, categoryProperty).toSet();
					for(RDFNode categoryResource : categoryResources)
					{
						if(categoryResource.asResource().getURI().equals(categoryURI))
						{
							templateContextToBeUpdated = tempContext.asResource();
							property = this.getProperty("is_mandatory");
							this.defaultOntologyModel.removeAll(templateContextToBeUpdated, property, null);
							templateContextToBeUpdated.addLiteral(property, mandatory);
							break;
						}
					}
					break;
				}
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Setting Descriptor / Descriptor Group " + descriptorURI 
				+ " as mandatory value " + mandatory 
				+ " in category " + categoryURI 
				+ " for Template " + templateURI + " in the ontology model.");
		//        Resource resource = dgContextToBeUpdated.getPropertyResourceValue(property);
		//        Literal previousLiteralValue = resource.asLiteral();
		//        boolean previousValue = previousLiteralValue.getBoolean();
		//        this.defaultOntologyModel.removeAll(dgContextToBeUpdated, property, null);
		//        templateContextToBeUpdated.addLiteral(property, mandatory);
	}


	@SuppressWarnings("deprecation")
	public void addDescriptorOrGroupToCategoryTemplate(String templateURI,
			String categoryURI, String descriptorURI, Integer maxOcurrence,
			boolean mandatory)
	{
		logger.debug("- START : Adding Descriptor / Descriptor Group " + descriptorURI 
				+ " for Template " + templateURI
				+ " in category " + categoryURI + " in the ontology model.");
		try
		{
			Individual templateIndiv = this.defaultOntologyModel.getIndividual(templateURI);
			Property property = this.getProperty("has_template_context");
			//        Set<RDFNode> templateContexts = this.defaultOntologyModel.listObjectsOfProperty(templateIndiv, property).toSet();
			property = this.getProperty("has_template_descriptor");
			String templateContextToBeUpdatedURI = null;
			Individual templateContextIndiv = null;
			//        for(RDFNode tempContext : templateContexts)
			//        {
			//            if(tempContext.asResource().getPropertyResourceValue(property).getURI().equals(descriptorURI))
			//            {
			//                templateContextIndiv = this.defaultOntologyModel.getIndividual(tempContext.asResource().getURI());
			//                break;
			//            }
			//        }
			//        if(templateContextIndiv == null)
			//        {
			templateContextToBeUpdatedURI = this.createNewIndividualURI("Template Context");
			templateContextIndiv = this.defaultOntologyModel.createIndividual(templateContextToBeUpdatedURI, 
					this.defaultOntologyModel.getOntClass(OntologyManager.TEMPLATE_CONTEXT_CLASS_URI));
			templateContextIndiv.setLabel(templateContextToBeUpdatedURI.substring(OntologyManager.baseURI.length()), null);

			property = this.getProperty("has_template_context");
			templateIndiv.addProperty(property, templateContextIndiv);
			property = this.getProperty("has_template_descriptor");
			templateContextIndiv.addProperty(property, this.getResource(descriptorURI));
			//        }

			property = this.getProperty("has_category");
			templateContextIndiv.addProperty(property, this.getResource(categoryURI));

			property = this.getProperty("is_mandatory");
			this.defaultOntologyModel.removeAll(templateContextIndiv, property, null);
			this.defaultOntologyModel.addLiteral(templateContextIndiv, property, mandatory);

			if(maxOcurrence != null)
			{
				property = this.getProperty("has_abbundance");
				this.defaultOntologyModel.removeAll(templateContextIndiv, property, null);
				this.defaultOntologyModel.addLiteral(templateContextIndiv, property, maxOcurrence);
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Adding Descriptor / Descriptor Group " + descriptorURI 
				+ " for Template " + templateURI
				+ " in category " + categoryURI + " in the ontology model.");
	}


	public void removeDescriptorOrGroupFromTemplate(String templateURI, String categoryURI,
			String descriptorURI)
	{
		logger.debug("- START : Removing Descriptor / Descriptor Group " + descriptorURI 
				+ " for Template " + templateURI
				+ " in category " + categoryURI + " in the ontology model.");
		try
		{
			Individual templateIndiv = this.defaultOntologyModel.getIndividual(templateURI);
			Property property = this.getProperty("has_template_context");
			Set<RDFNode> templateContexts = this.defaultOntologyModel.listObjectsOfProperty(templateIndiv, property).toSet();
			property = this.getProperty("has_template_descriptor");
			Property categoryProperty = this.getProperty("has_category");
			Resource templateContextToBeUpdated = null;
			Individual templateContextIndiv = null;
			for(RDFNode tempContext : templateContexts)
			{
				if(tempContext.asResource().getPropertyResourceValue(property).getURI().equals(descriptorURI))
				{
					templateContextIndiv = this.defaultOntologyModel.getIndividual(tempContext.asResource().getURI());
					Set<RDFNode> categoryResources = this.defaultOntologyModel.listObjectsOfProperty(templateContextIndiv, categoryProperty).toSet();
					for(RDFNode categoryResource : categoryResources)
					{
						if(categoryResource.asResource().getURI().equals(categoryURI))
						{
							templateContextToBeUpdated = tempContext.asResource();
							//just remove its category as there might be more categories for this context
							this.defaultOntologyModel.removeAll(templateContextToBeUpdated, 
									categoryProperty, this.getResource(categoryURI));
							break;
						}
					}
					break;
				}
			}


			Set<RDFNode> categoryResources = this.defaultOntologyModel.listObjectsOfProperty(templateContextIndiv, categoryProperty).toSet();
			if(categoryResources.isEmpty()) // no more categories for this context
			{
				this.defaultOntologyModel.removeAll(null, null, templateContextToBeUpdated);
				this.defaultOntologyModel.removeAll(templateContextToBeUpdated, null, null);
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Removing Descriptor / Descriptor Group " + descriptorURI 
				+ " for Template " + templateURI
				+ " in category " + categoryURI + " in the ontology model.");
	}


	/**
	 * create a new instance (individual) in the ontology for the given class
	 * @param classUri class URI of the individual (existing URI from the ontology)
	 * @param label label for the new individual
	 * @return the new URI for the created individual
	 */
	public String createNewIndividual(String classUri, String label)
	{
		try
		{
			logger.debug("- START : Creating a new Individual for class " + classUri + " with label " + label + " in the ontology model.");

			String indivURI = this.createNewIndividualURI(classUri.substring(OntologyManager.baseURI.length()));
			Individual indiv = this.defaultOntologyModel.createIndividual(indivURI, 
					this.defaultOntologyModel.getOntClass(classUri));
			indiv.setLabel(label, null);

			logger.debug("- END   : Creating a new Individual for class " + classUri + " with label " + label + " in the ontology model.");

			return indiv.getURI();

		} catch (Exception ex)
		{
			logger.error(ex);
			throw ex;
		}
	}


	public void removeIndividual(String indivURI)
	{
		try
		{
			logger.debug("- START : Removing the individual " + indivURI + " from the ontology model.");

			Individual indivToBeRemoved = this.defaultOntologyModel.getIndividual(indivURI);
			switch(indivToBeRemoved.getOntClass().getURI())
			{
			case OntologyManager.DESCRIPTOR_CLASS_URI :
				this.removeIndividualsAsSubjects("has_descriptor", indivURI);
			case OntologyManager.DESCRIPTOR_GROUP_CLASS_URI :
				this.removeIndividualsAsSubjects("has_template_descriptor", indivURI);
				break;
			case OntologyManager.TEMPLATE_CLASS_URI :
				this.removeIndividualsAsObjects(indivURI, "has_template_context");
				break;
			}
			indivToBeRemoved.remove();

			logger.debug("- END   : Removing the individual " + indivURI + " from the ontology model.");

		} catch (Exception ex)
		{
			logger.error(ex);
			throw ex;
		}
		//        this.defaultOntologyModel.removeAll(null, null, this.defaultOntologyModel.getIndividual(uri));
		//        this.defaultOntologyModel.removeAll(this.defaultOntologyModel.getResource(uri), null, null);
	}


	private void removeIndividualsAsSubjects(String propertyName, String objectURI)
	{
		Resource objectResource = this.defaultOntologyModel.getResource(objectURI);
		Property property = this.defaultOntologyModel.getProperty(OntologyManager.baseURI + propertyName);
		List<Resource> resources = this.defaultOntologyModel.listSubjectsWithProperty(property, objectResource).toList();
		Individual indivToBeRemoved = null;
		for(Resource resource : resources)
		{
			indivToBeRemoved = this.defaultOntologyModel.getIndividual(resource.getURI());
			indivToBeRemoved.remove();
		}
	}

	private void removeIndividualsAsObjects(String subjectURI, String propertyName)
	{
		Resource subjectResource = this.defaultOntologyModel.getResource(subjectURI);
		Property property = this.defaultOntologyModel.getProperty(OntologyManager.baseURI + propertyName);
		List<RDFNode> rdfNodes = this.defaultOntologyModel.listObjectsOfProperty(subjectResource, property).toList();
		Individual indivToBeRemoved = null;
		for(RDFNode rdfNode : rdfNodes)
		{
			indivToBeRemoved = this.defaultOntologyModel.getIndividual(rdfNode.asResource().getURI());
			indivToBeRemoved.remove();
		}
	}

	//    public void removeDescriptorOrGroupFromTemplate(
	//            ClassesWithFeatures classWithFeatures)
	//    {
	//        // TODO Auto-generated method stub
	//        
	//    }


	/**
	 * return a list of all namespaces
	 * 
	 * @return list of namespaces
	 */
	public List<Namespace> getAllNamespaces()
	{
		List<Namespace> namespaces = new ArrayList<Namespace>();

		logger.debug("- START : Retrieving all Namespaces from the ontology model.");

		try
		{
			Set<Resource> instances = null;
			DatatypeProperty hasNamespaceFileProperty = null;
			DatatypeProperty hasNamespaceIdFileProperty = null;
			OntModel model = null;
			if(managerMode)
			{
				instances = (Set<Resource>) this.defaultOntologyModel.listSubjectsWithProperty(
						RDF.type, this.defaultOntologyModel.getResource(
								GritsOntologyManagerApi.NAMESPACE_CLASS_URI)).toSet();
				hasNamespaceFileProperty =  
						defaultOntologyModel.getDatatypeProperty(OntologyManager.baseURI+"has_namespace_file");
				hasNamespaceIdFileProperty = 
						defaultOntologyModel.getDatatypeProperty(OntologyManager.baseURI+"has_namespace_id_file");
				model = defaultOntologyModel;
			}
			else{
				instances = (Set<Resource>) this.readOnlyOntologyModel.listSubjectsWithProperty(
						RDF.type, this.readOnlyOntologyModel.getResource(
								GritsOntologyManagerApi.NAMESPACE_CLASS_URI)).toSet();
				hasNamespaceFileProperty =  
						readOnlyOntologyModel.getDatatypeProperty(OntologyManager.baseURI+"has_namespace_file");
				hasNamespaceIdFileProperty = 
						readOnlyOntologyModel.getDatatypeProperty(OntologyManager.baseURI+"has_namespace_id_file");
				model = readOnlyOntologyModel;
			}
			Individual indiv = null;
			for(Resource instance : instances)
			{
				indiv = this.getIndividual(instance.getURI());
				if(indiv != null)
				{
					String namespaceFile = null;
					String namespaceIdFile = null;
					List<RDFNode> nodes = model.listObjectsOfProperty(indiv, hasNamespaceFileProperty).toList();
					if(!nodes.isEmpty())
					{
						namespaceFile = nodes.get(0).asLiteral().toString();
						nodes = model.listObjectsOfProperty(indiv, hasNamespaceIdFileProperty)
								.toList();
						namespaceIdFile = nodes.get(0).asLiteral().toString();
					}
					namespaces.add(new Namespace(indiv.getURI(), indiv.getLabel(null), namespaceFile, namespaceIdFile));
				}
				else
				{
					String label = instance.getLocalName();
					namespaces.add(new Namespace(instance.getURI(), label, null, null));
				}
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}

		logger.debug("- END   : Retrieving all Namespaces from the ontology model.");

		return namespaces;
	}


	/**
	 * add the given namespace to the given descriptor (this method only attaches namespace to the descriptor, does not create any new individuals)
	 * @param descriptorURI URI of the descriptor
	 * @param namespaceURI URI of the namespace
	 */
	public void addNamespaceToDescriptor(String descriptorURI, String namespaceURI)
	{
		Set<Resource> instances = (Set<Resource>) this.defaultOntologyModel.listSubjectsWithProperty(
				RDF.type, this.defaultOntologyModel.getResource(
						GritsOntologyManagerApi.NAMESPACE_CLASS_URI)).toSet();
		Resource subjectResource = this.defaultOntologyModel.getResource(descriptorURI);
		ObjectProperty objectProperty = 
				this.defaultOntologyModel.getObjectProperty(OntologyManager.baseURI + "has_namespace");
		Individual indiv = null;
		for(Resource instance : instances)
		{
			if(instance.getURI().equals(namespaceURI))
			{
				indiv = this.getIndividual(instance.getURI());
				if(indiv != null)
				{
					if(subjectResource != null)
					{
						subjectResource.addProperty(objectProperty, indiv);
					}
					break;
				}
				else if(instance.getURI().startsWith("http://www.w3.org/2001/XMLSchema"))
				{
					subjectResource.addProperty(objectProperty, instance);
				}
			}
		}
//		this.addObjectPropertyStatement(descriptorURI, "has_namespace", namespaceURI);
	}

	/**
	 * update multiplicity of the given descriptor in a given group
	 * 
	 * @param groupURI URI of the descriptor group
	 * @param descriptorURI URI of the descriptor
	 * @param maxOccurrence multiplicity to set
	 */
	public void setMaxOccurrenceInGroup(String groupURI, String descriptorURI, Integer maxOccurrence)
	{
		logger.debug("- START : Updating Max Occurrence for Descriptor " + descriptorURI 
				+ " in Group " + groupURI
				+ " as maxOccurrence " + maxOccurrence + " in the ontology model.");
		try
		{
			Individual groupIndiv = this.defaultOntologyModel.getIndividual(groupURI);
			Property property = this.getProperty("has_descriptor_group_context");
			Set<RDFNode> dgContexts = this.defaultOntologyModel.listObjectsOfProperty(groupIndiv, property).toSet();
			Individual descriptorIndiv = this.getIndividual(descriptorURI);
			property = this.getProperty("has_descriptor");
			Resource dgContextToBeUpdated = null;
			for(RDFNode dgContext : dgContexts)
			{
				if(dgContext.asResource().getPropertyResourceValue(property).getURI().equals(descriptorIndiv.getURI()))
				{
					dgContextToBeUpdated = dgContext.asResource();
					property = this.getProperty("has_abbundance");
					this.defaultOntologyModel.removeAll(dgContextToBeUpdated, property, null);
					if(maxOccurrence != null)
						dgContextToBeUpdated.addLiteral(property, maxOccurrence);
					break;
				}
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Updating Max Occurrence for Descriptor " + descriptorURI 
				+ " in Group " + groupURI
				+ " as maxOccurrence maxOccurrence " + maxOccurrence + " in the ontology model.");
	}

	public void setMaxOccurrenceInTemplate(String templateURI, String categoryURI, 
			String descriptorOrGroupURI, Integer maxOccurrence)
	{
		logger.debug("- START : Updating Max Occurrence for Descriptor / Descriptor Group " + descriptorOrGroupURI 
				+ " for Template " + descriptorOrGroupURI
				+ " in Category " + categoryURI
				+ " as " + maxOccurrence + " in the ontology model.");
		try
		{
			Individual tempIndiv = this.defaultOntologyModel.getIndividual(templateURI);
			Property property = this.getProperty("has_template_context");
			Set<RDFNode> tempContexts = this.defaultOntologyModel.listObjectsOfProperty(tempIndiv, property).toSet();
			Individual descriptorIndiv = this.getIndividual(descriptorOrGroupURI);
			Individual categoryIndividual = this.getIndividual(categoryURI);
			property = this.getProperty("has_template_descriptor");
			Property categoryProperty = this.getProperty("has_category");
			Resource tempContextToBeUpdated = null;
			for(RDFNode tempContext : tempContexts)
			{
				if(tempContext.asResource().getPropertyResourceValue(property).getURI()
						.equals(descriptorIndiv.getURI())
						&& tempContext.asResource().getPropertyResourceValue(categoryProperty).getURI()
						.equals(categoryIndividual.getURI()))
				{
					tempContextToBeUpdated = tempContext.asResource();
					property = this.getProperty("has_abbundance");
					this.defaultOntologyModel.removeAll(tempContextToBeUpdated, property, null);
					if(maxOccurrence != null)
						tempContextToBeUpdated.addLiteral(property, maxOccurrence);
					break;
				}
			}
		} catch (Exception ex)
		{
			logger.error(ex);
		}
		logger.debug("- END   : Updating Max Occurrence for Descriptor / Descriptor Group " + descriptorOrGroupURI 
				+ " for Template " + descriptorOrGroupURI
				+ " in Category " + categoryURI
				+ " as maxOccurrence " + maxOccurrence + " in the ontology model.");
	}

}
