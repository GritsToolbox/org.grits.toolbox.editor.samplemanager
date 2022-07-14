package org.grits.toolbox.editor.samplemanager.ontology;
/**
 * 
 */


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.grits.toolbox.entry.sample.model.CategoryTemplate;
import org.grits.toolbox.entry.sample.model.Descriptor;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;
import org.grits.toolbox.entry.sample.model.MeasurementUnit;
import org.grits.toolbox.entry.sample.model.Namespace;
import org.grits.toolbox.entry.sample.model.Template;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;


/**
 * 
 *
 */
public class OntologyManager 
{
	public static final String baseURI = "http://www.grits-toolbox.org/ontology/sample#";
	public static final String baseURIWithoutHash = "http://www.grits-toolbox.org/ontology/sample";

	public static final String CATEGORY_SAMPLE_INFO_CLASS_URI = baseURI + "category_sample";
	public static final String CATEGORY_TRACKING_INFO_CLASS_URI = baseURI + "category_sample_tracking";
	public static final String CATEGORY_AMOUNT_CLASS_URI = baseURI + "category_amount";
	public static final String CATEGORY_PURITY_QC_CLASS_URI = baseURI + "category_qc";

	public static final String DESCRIPTOR_CLASS_URI = baseURI + "descriptor";
	public static final String DESCRIPTOR_GROUP_CLASS_URI = baseURI + "descriptor_group";
	public static final String TEMPLATE_CLASS_URI = baseURI + "template";
	public static final String UNIT_CLASS_URI = baseURI + "unit";

	public static final String CATEGORY_CLASS_URI = baseURI + "category";
	public static final String TEMPLATE_CONTEXT_CLASS_URI = baseURI + "template_context";
	public static final String MEASUREMENT_UNIT_CLASS_URI = baseURI + "unit";
	public static final String NAMESPACE_CLASS = baseURI + "namespace";
	public static final String GUIDELINE_CLASS = baseURI + "StandardGuideline";

	public OntModel standardOntologymodel = null;
	public OntModel localOntologymodel = null;

	public OntologyManager(OntModel localOntologyModel)
	{
		this.localOntologymodel = localOntologyModel;
	}

	public OntologyManager(OntModel standardOntologyModel,
			OntModel localOntologyModel)
	{
		this.standardOntologymodel = standardOntologyModel;
		this.localOntologymodel = localOntologyModel;
	}
	public OntologyManager(InputStream standardInputOntology, InputStream locaInputOntology) {
		this.standardOntologymodel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);
		this.standardOntologymodel.read(standardInputOntology, OntologyManager.baseURI);
		this.localOntologymodel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);
		this.localOntologymodel.read(locaInputOntology, OntologyManager.baseURI);
	}

	public void writeOntology (OntModel model, OutputStream outputOntology){
		model.write(outputOntology);
	}

	public void addProperty(OntModel model, String subjectUri, String property,
			String objectUri) {
		Individual indiv = model.getIndividual(subjectUri);
		String propertyUri  = baseURI + property;
		Property prop = model.getObjectProperty(propertyUri);
		if(model.getObjectProperty(propertyUri) != null) {
			indiv.addProperty(prop, model.getIndividual(objectUri));
		}
	}


	public void addLiteral(OntModel model, String subjectUri, String property,
			Literal literalValue) {
		Individual indiv = model.getIndividual(subjectUri);
		String propertyUri  = baseURI + property;
		Property prop = model.getProperty(propertyUri);
		//		literalValue = expandPrefixes(literalValue);
		if(prop!=null) {
			indiv.addLiteral(prop, literalValue);
		}
	}

	public List<Individual> getAllIndependentDescriptors(OntModel model) {
		List<Individual> individuals = this.getAllIndiviudalsOfClass(model, DESCRIPTOR_CLASS_URI);
		List<RDFNode> dependentDescriptors = model.listObjectsOfProperty(model.getProperty(baseURI + "has_descriptor")).toList();
		List<String> dependentDescriptorsUris = new ArrayList<String>();
		for(RDFNode node : dependentDescriptors) {
			dependentDescriptorsUris.add(node.toString());
		}
		List<Individual> independentIndividuals = new ArrayList<Individual>();
		for(Individual indiv : individuals) {
			if(!dependentDescriptorsUris.contains(indiv.getURI())) {
				independentIndividuals.add(indiv);
			}
		}
		return independentIndividuals;
	}

	public Literal getLiteralValue(OntModel model, Individual indiv, String propertyName) {
		Literal val = null;
		RDFNode node = indiv.getPropertyValue(model.getProperty(baseURI + propertyName));
		if(node!= null && node.isLiteral()) {
			val = node.asLiteral();
		}
		return val;
	}

	public List<Literal> getLiteralValues(OntModel model, Individual indiv, String propertyName) {
		List<Literal> literals = new ArrayList<Literal>();
		NodeIterator nodeIterator = indiv.listPropertyValues(model.getProperty(baseURI + propertyName));
		RDFNode node = null;
		while(nodeIterator.hasNext()) {
			node = nodeIterator.next();
			if(node.isLiteral()) {
				literals.add(node.asLiteral());
			}
		}
		return literals;
	}

	public List<Individual> getAllDescriptorGroup(OntModel model) {
		List<Individual> individuals = this.getAllIndiviudalsOfClass(model, DESCRIPTOR_GROUP_CLASS_URI);		
		return individuals;
	}


	public List<Individual> getAllTemplates(OntModel model) {
		List<Individual> individuals = this.getAllIndiviudalsOfClass(model, TEMPLATE_CLASS_URI);		
		return individuals;
	}

	public List<Individual> getAllIndiviudalsOfClass(OntModel model, String classUri) {
		OntClass thisClass = model.getOntClass(classUri);
		Set<Individual> individuals = model.listIndividuals(thisClass).toSet();
		List<Individual> actualIndividuals = new ArrayList<>(individuals);
		return actualIndividuals;
	}

	public List<Individual> getAllObjects(OntModel model, Individual subject,
			String property) {

		NodeIterator objectIterator = model.listObjectsOfProperty(subject, model.getProperty(baseURI + property));
		List<Individual> objects = new ArrayList<Individual>();
		for(RDFNode n : objectIterator.toList()) {
			String uri = n.asResource().getURI();
			Individual indiv = model.getIndividual(uri);
			objects.add(indiv);
		}
		return objects;
	}

	public Individual getObject(OntModel model, Individual individual, String propertyUri) {
		Property property = model.getProperty(baseURI + propertyUri);
		NodeIterator objects = model.listObjectsOfProperty(individual, property);
		Individual objectIndiv = null;
		if(objects.hasNext()) {
			objectIndiv = model.getIndividual(objects.next().asResource().getURI());
		}
		return objectIndiv;
	}

	/**
	 * For use in JaxB
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	 public Descriptor getDescriptorByUri(OntModel model, String uri) throws Exception {
		Individual indiv = model.getIndividual(uri);
		if(indiv != null) {
			String classUri = indiv.getOntClass().getURI();
			if(indiv.getOntClass().getURI().equals(DESCRIPTOR_CLASS_URI)) {
				Descriptor descriptor = new Descriptor();
				descriptor.setUri(uri);
				descriptor.setLabel(indiv.getLabel(null));
				descriptor.setDescription(indiv.getComment(null));

				Literal maxOccurrence = this.getLiteralValue(model, indiv, "has_abbundance");
				if(maxOccurrence != null)
					descriptor.setMaxOccurrence(maxOccurrence.getInt());
				
				Literal position = this.getLiteralValue(model, indiv, "has_position");
				int pos = 0;
				if (position != null) {
					pos = position.getInt();
					descriptor.setPosition(pos);
				}
				
				List<Individual> guidelinesFromContext = this.getAllObjects(model, indiv, "guideline_info");
				if (guidelinesFromContext != null) {
					List<String> guidelines = new ArrayList<>();
					for(Individual guideline : guidelinesFromContext) {
						guidelines.add(guideline.getLabel(null));
					}
					descriptor.setGuidelineURIs(guidelines);
				}

				List<Namespace> namespaces = this.getNamespacesForDescriptor(model, indiv);
				descriptor.setNamespaces(namespaces);


				List<Individual> units = this.getAllObjects(model, indiv, "has_default_unit_of_measurement");
				if(!units.isEmpty())
				{
					//only one unit is default
					descriptor.setDefaultMeasurementUnit(units.iterator().next().getURI());
				}

				units = this.getAllObjects(model, indiv, "has_unit_of_measurement");
				MeasurementUnit mUnit;
				for(Individual unit : units) {
					mUnit = new MeasurementUnit(unit.getURI(), unit.getLabel(null));
					descriptor.addValidUnit(mUnit);
				}

				List<Individual> categories = this.getAllObjects(model, indiv, "has_category");
				for(Individual category : categories) {
					descriptor.addCategory(category.getURI());
				}

				return descriptor;
			}
			else {
				throw new Exception("The given uri" + uri  + classUri + "is not of type Descriptor but is of type " + indiv.getOntClass().getURI());
			}
		}
		else {
			return null;
		}
	 }


	 public DescriptorGroup getDescriptorGroupByUri(OntModel model, String uri) throws Exception {
		 Individual indiv = model.getIndividual(uri);
		 if(indiv != null) {
			 if(indiv.getOntClass().getURI().equals(DESCRIPTOR_GROUP_CLASS_URI)) {
				 DescriptorGroup descriptorGroup = new DescriptorGroup();
				 descriptorGroup.setUri(uri);
				 descriptorGroup.setLabel(indiv.getLabel(null));
				 descriptorGroup.setDescription(indiv.getComment(null));
				 Literal maxOccurrence = this.getLiteralValue(model, indiv, "has_abbundance");
				 if(maxOccurrence != null)
					 descriptorGroup.setMaxOccurrence(maxOccurrence.getInt());
				 
				 Literal position = this.getLiteralValue(model, indiv, "has_position");
				 int pos = 0;
				 if (position != null) {
					 pos = position.getInt();
					 descriptorGroup.setPosition(pos);
				 }
					
				 List<Individual> guidelinesFromContext = this.getAllObjects(model, indiv, "guideline_info");
				 if (guidelinesFromContext != null) {
					 List<String> guidelines = new ArrayList<>();
					 for(Individual guideline : guidelinesFromContext) {
						 guidelines.add(guideline.getLabel(null));
					 }
					 descriptorGroup.setGuidelineURIs(guidelines);
				 }	

				 List<Individual> categories = this.getAllObjects(model, indiv, "has_category");
				 for(Individual category : categories) {
					 descriptorGroup.addCategory(category.getURI());
				 }

				 Descriptor descriptor;
				 Individual descriptorIndiv;
				 List<Individual> dgContextIndividuals = this.getAllObjects(model, indiv, "has_descriptor_group_context");
				 for(Individual dgContext : dgContextIndividuals) {
					 descriptor = new Descriptor();
					 descriptorIndiv = this.getObject(model, dgContext, "has_descriptor");
					 descriptor.setUri(descriptorIndiv.getURI());
					 descriptor.setLabel(descriptorIndiv.getLabel(null));
					 descriptor.setDescription(descriptorIndiv.getComment(null));

					 List<Namespace> namespaces = this.getNamespacesForDescriptor(model, descriptorIndiv);
					 descriptor.setNamespaces(namespaces);

					 List<Individual> units = this.getAllObjects(model, descriptorIndiv, "has_default_unit_of_measurement");
					 if(!units.isEmpty())
					 {
						 //only one unit is default
						 descriptor.setDefaultMeasurementUnit(units.iterator().next().getURI());
					 }

					 // changed from context to descriptor's units 
					 // as manager does not allow changing unit for context 
					 units = this.getAllObjects(model, descriptorIndiv, "has_unit_of_measurement");
					 MeasurementUnit mUnit;
					 for(Individual unit : units) {
						 mUnit = new MeasurementUnit(unit.getURI(), unit.getLabel(null));
						 descriptor.addValidUnit(mUnit);
					 }

					 // context over-writes the abundance of the descriptor
					 maxOccurrence = this.getLiteralValue(model, dgContext, "has_abbundance");
					 if(maxOccurrence != null)
						 descriptor.setMaxOccurrence(maxOccurrence.getInt());
					 else
						 descriptor.setMaxOccurrence(null);

					 Literal mandatoryLiteral = this.getLiteralValue(model, dgContext, "is_mandatory");
					 if(mandatoryLiteral.getBoolean())
						 descriptorGroup.addMandatoryDescriptor(descriptor);
					 else
						 descriptorGroup.addOptionalDescriptor(descriptor);
				 }
				 return descriptorGroup;
			 }
			 else {
				 throw new Exception("The given uri " + uri +  " is not of type descriptorGroup but is of type " + indiv.getOntClass().getURI());
			 }
		 }
		 else {
			 return null;
		 }
	 }

	 private List<Namespace> getNamespacesForDescriptor(OntModel model, Individual descriptorIndiv)
	 {
		 NodeIterator objectIterator = model.listObjectsOfProperty(descriptorIndiv, 
				 model.getProperty(baseURI + "has_namespace"));
		 List<Namespace> namespaces = new ArrayList<Namespace>();
		 Individual nsIndiv = null;
		 String nsURI = null;
		 for(RDFNode n : objectIterator.toList()) {
			 nsURI = n.asResource().getURI();
			 nsIndiv = model.getIndividual(nsURI);
			 if(nsIndiv != null)
			 {
				 String namespaceFile = null;
				 String namespaceIdFile = null;
				 List<RDFNode> nodes = model.listObjectsOfProperty(nsIndiv, 
						 model.getDatatypeProperty(baseURI+"has_namespace_file")).toList();
				 if(!nodes.isEmpty())
				 {
					 namespaceFile = nodes.get(0).asLiteral().toString();
					 nodes = model.listObjectsOfProperty(nsIndiv, 
							 model.getDatatypeProperty(baseURI+"has_namespace_id_file")).toList();
					 if (!nodes.isEmpty()) namespaceIdFile = nodes.get(0).asLiteral().toString();
				 }
				 namespaces.add(new Namespace(nsURI, nsIndiv.getLabel(null), namespaceFile, namespaceIdFile));
			 }
			 else 
			 {
				 namespaces.add(new Namespace(nsURI, n.asResource().getLocalName(), null, null));
			 }
		 }
		 return namespaces;
	 }

	 public Set<String> getAllSubjectURIs(OntModel model, String propertyLabel, String objectUri) {
		 Property property = model.getProperty(baseURI + propertyLabel);
		 ResIterator subjectIterator = model.listSubjectsWithProperty(property, model.getIndividual(objectUri));
		 Set<String> subjects = new HashSet<String>();
		 while(subjectIterator.hasNext()) {
			 subjects.add(subjectIterator.next().getURI());
		 }
		 return subjects;
	 }

	 public List<Individual> getAllSubjects(OntModel model, String propertyLabel, String objectUri) {
		 Property property = model.getProperty(baseURI + propertyLabel);
		 ResIterator subjectIterator = model.listSubjectsWithProperty(property, model.getIndividual(objectUri));
		 Resource subject;
		 List<Individual> subjects = new ArrayList<Individual>();
		 while(subjectIterator.hasNext()) {
			 subject = subjectIterator.next();
			 if(model.getIndividual(subject.getURI())!=null) {
				 subjects.add(model.getIndividual(subject.getURI()));
			 }
		 }
		 return subjects;
	 }

	 public Template getTemplate(OntModel model, String uri) throws Exception {
		 Template template = null;
		 Individual indiv = model.getIndividual(uri);
		 if(indiv != null) {
			 if(indiv.getOntClass().getURI().equals(TEMPLATE_CLASS_URI)) {
				 template =  new Template();
				 template.setUri(uri);
				 template.setLabel(indiv.getLabel(null));
				 template.setDescription(indiv.getComment(null));

				 Literal glycovaultId = this.getLiteralValue(model, indiv, "has_glycovault_id");
				 if(glycovaultId != null)
					 template.setGlycovaultId(glycovaultId.getInt());

				 Individual indivFromContext;
				 List<Individual> templateContexts = this.getAllObjects(model, indiv, "has_template_context");
				 boolean isManadatory;
				 DescriptorGroup descriptorGroup;
				 Descriptor descriptor;
				 List<Individual> categories;
				 Literal maxOccurrence;
				 String categoryUri;
				 String descUri;
				 CategoryTemplate sampleInfoTemplate = new CategoryTemplate();
				 CategoryTemplate amountTemplate = new CategoryTemplate();
				 CategoryTemplate purityQCTemplate = new CategoryTemplate();
				 CategoryTemplate trackingTemplate = new CategoryTemplate();
				 amountTemplate.setUri(CATEGORY_AMOUNT_CLASS_URI);
				 amountTemplate.setTemplateURI(indiv.getURI());
				 purityQCTemplate.setUri(CATEGORY_PURITY_QC_CLASS_URI);
				 purityQCTemplate.setTemplateURI(indiv.getURI());
				 sampleInfoTemplate.setUri(CATEGORY_SAMPLE_INFO_CLASS_URI);
				 sampleInfoTemplate.setTemplateURI(indiv.getURI());
				 trackingTemplate.setUri(CATEGORY_TRACKING_INFO_CLASS_URI);
				 trackingTemplate.setTemplateURI(indiv.getURI());
				 for(Individual templateContext : templateContexts) {
					 indivFromContext = this.getObject(model, templateContext, "has_template_descriptor");
					 indivFromContext = this.getResourceIndiv(indivFromContext.getURI());
					 isManadatory = this.getLiteralValue(model, templateContext, "is_mandatory").getBoolean();
					 maxOccurrence = this.getLiteralValue(model, templateContext, "has_abbundance");

					 categories = this.getAllObjects(model, templateContext, "has_category");

					 for(Individual category : categories) 
					 {
						 categoryUri = category.getURI();
						 descUri = indivFromContext.getOntClass().getURI();
						 List<String> singleCategory = new ArrayList<String>();
						 singleCategory.add(categoryUri);
						 if(categoryUri!=null) 
						 {
							 if(descUri.equals(DESCRIPTOR_GROUP_CLASS_URI)) {
								 descriptorGroup = this.getDescriptorGroupFromEitherOntology(indivFromContext.getURI());

								 // template context overwrites the maxOccurrence of the descriptor group
								 if(maxOccurrence != null)
									 descriptorGroup.setMaxOccurrence(maxOccurrence.getInt());
								 else
									 descriptorGroup.setMaxOccurrence(null);

								 //template context changes default categories
								 descriptorGroup.setCategories(singleCategory);

								 if (categoryUri.equals(CATEGORY_SAMPLE_INFO_CLASS_URI)) {
									 if(isManadatory)
										 sampleInfoTemplate.addMandatoryDescriptorGroup(descriptorGroup);
									 else 
										 sampleInfoTemplate.addOptionalDescriptorGroup(descriptorGroup);
								 } else if (categoryUri.equals(CATEGORY_AMOUNT_CLASS_URI)) {
									 if(isManadatory)
										 amountTemplate.addMandatoryDescriptorGroup(descriptorGroup);
									 else 
										 amountTemplate.addOptionalDescriptorGroup(descriptorGroup);
								 } else if (categoryUri.equals(CATEGORY_PURITY_QC_CLASS_URI)) {
									 if(isManadatory)
										 purityQCTemplate.addMandatoryDescriptorGroup(descriptorGroup);
									 else 
										 purityQCTemplate.addOptionalDescriptorGroup(descriptorGroup);
								 } else if (categoryUri.equals(CATEGORY_TRACKING_INFO_CLASS_URI)) {
									 if(isManadatory)
										 trackingTemplate.addMandatoryDescriptorGroup(descriptorGroup);
									 else 
										 trackingTemplate.addOptionalDescriptorGroup(descriptorGroup);
								 }


							 }
							 else if(descUri.equals(DESCRIPTOR_CLASS_URI)) {
								 descriptor = this.getDescriptorFromEitherOntology(indivFromContext.getURI());

								 // template context overwrites the maxOccurrence of the descriptor
								 if(maxOccurrence != null)
									 descriptor.setMaxOccurrence(maxOccurrence.getInt());
								 else
									 descriptor.setMaxOccurrence(null);

								 //template context changes default categories
								 descriptor.setCategories(singleCategory);

								 if(categoryUri!=null) {
									 if (categoryUri.equals(CATEGORY_SAMPLE_INFO_CLASS_URI)) {
										 if(isManadatory)
											 sampleInfoTemplate.addMandatoryDescriptor(descriptor);
										 else 
											 sampleInfoTemplate.addOptionalDescriptor(descriptor);
									 } else if (categoryUri.equals(CATEGORY_AMOUNT_CLASS_URI)) {
										 if(isManadatory)
											 amountTemplate.addMandatoryDescriptor(descriptor);
										 else 
											 amountTemplate.addOptionalDescriptor(descriptor);
									 } else if (categoryUri.equals(CATEGORY_PURITY_QC_CLASS_URI)) {
										 if(isManadatory)
											 purityQCTemplate.addMandatoryDescriptor(descriptor);
										 else 
											 purityQCTemplate.addOptionalDescriptor(descriptor);
									 } else if (categoryUri.equals(CATEGORY_TRACKING_INFO_CLASS_URI)) {
										 if(isManadatory)
											 trackingTemplate.addMandatoryDescriptor(descriptor);
										 else 
											 trackingTemplate.addOptionalDescriptor(descriptor);
									 }
								 }
							 }
						 }
						 else
							 throw new Exception("category is not defined for " + templateContext.getURI());
					 }
				 }
				 template.setSampleInformationTemplate(sampleInfoTemplate);
				 template.setAmountTemplate(amountTemplate);
				 template.setPurityQCTemplate(purityQCTemplate);
				 template.setTrackingTemplate(trackingTemplate);
			 }
			 else {
				 throw new Exception("The given uri is not of type Template but is of type " + indiv.getOntClass().getURI());
			 }
		 }
		 return template;
	 }

	 public DescriptorGroup getDescriptorGroupFromEitherOntology(String uri) throws Exception
	 {
		 DescriptorGroup descriptorGroup = null;
		 if(standardOntologymodel != null)
		 {
			 descriptorGroup = this.getDescriptorGroupByUri(standardOntologymodel, uri);
		 } 
		 if (descriptorGroup == null)
		 {
			 descriptorGroup = this.getDescriptorGroupByUri(localOntologymodel, uri);
		 }
		 return descriptorGroup;
	 }

	 public Descriptor getDescriptorFromEitherOntology(String uri) throws Exception
	 {
		 Descriptor descriptor = null;
		 if(standardOntologymodel != null)
		 {
			 descriptor = this.getDescriptorByUri(standardOntologymodel, uri);
		 } 
		 if (descriptor == null)
		 {
			 descriptor = this.getDescriptorByUri(localOntologymodel, uri);
		 }
		 return descriptor;
	 }

	 public Individual getResourceIndiv(String uri)
	 {
		 Individual resourceIndividual = null;
		 if(standardOntologymodel != null)
		 {
			 resourceIndividual = standardOntologymodel.getIndividual(uri);
		 }
		 if(resourceIndividual == null)
		 {
			 resourceIndividual = localOntologymodel.getIndividual(uri);
		 }
		 return resourceIndividual;
	 }

}
