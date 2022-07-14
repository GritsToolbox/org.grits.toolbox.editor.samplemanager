/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.ontology;

import java.util.List;
import java.util.Set;

import org.grits.toolbox.entry.sample.model.MeasurementUnit;

import com.hp.hpl.jena.shared.AlreadyExistsException;
import com.hp.hpl.jena.shared.NotFoundException;

/**
 * This interface provides a list of essential methods for editing sample ontology.
 * One of the class that implements this interface in this
 * <b>"org.grits.toolbox.editor.samplemanager"</b> plugin is : {@link SampleOntologyEditApi}
 * 
 */
public interface ISampleOntologyEditApi
{
	/**
	 * create a new individual in the ontology (uses <b>local ontology model</b>
	 * if there is more than one <b>model</b>).
	 * In general, for all write statements which has no previous uri relationships,
	 * if there are two <b>ontology models</b> then by default the <b>local ontology model</b> is used.
	 * @param classUri uri of the class of the individual to be created
	 * @param label label of the individual to be created
	 * @return uri of the individual after being successfully created
	 * or else returns <b>null</b> if something went wrong while creating the individual.
	 * @throws AlreadyExistsException if an individual in the given class with the
	 * same label already exists in its ontology
	 */
	public String createNewIndividual(String classUri, String label) throws AlreadyExistsException;

	/**
	 * returns a set of uris of all the individuals belonging to the given class
	 * from local ontology
	 * @return a set of string (could be empty) or else returns null if
	 * class uri is null or is not a valid class in local ontology
	 */
	public Set<String> getLocalIndividualURIs(String classUri);

	/**
	 * update label for an individual of the given uri
	 * @param uri of the individual whose label is to be updated
	 * @param label that is to be set
	 * @return true if updated successfully else returns false
	 * @throws NotFoundException if any individual with given uri was not found in the ontology
	 */
	public boolean updateLabel(String uri, String label) throws NotFoundException;

	/**
	 * update description for an individual of the given uri
	 * @param uri of the individual whose description is to be updated
	 * @param description that is to be set
	 * @return true if updated successfully else returns false
	 * @throws NotFoundException if any individual with given uri was not found in the ontology
	 */
	public boolean updateDescription(String uri, String description) throws NotFoundException;

	/**
	 * updates category for descriptor or a descriptor group
	 * @param categoryUri uri of the category to be added or removed
	 * @param descriptorOrGroupUri uri of the descriptor or descriptor group
	 * @param selected if true adds this category else removes this category for the
	 * descriptor or descriptor group
	 * @return true if adding or removal was successful else returns false
	 */
	public boolean updateCategorySelection(String categoryUri, String descriptorOrGroupUri, boolean selected);

	/**
	 * adds a measurement unit to an existing descriptor
	 * @param descriptorUri the uri of the descriptor to which unit is to be added
	 * @param measurementUnit the measurement unit that is to be added
	 * @return true if it was successfully added else returns false
	 */
	public boolean addUnitToDescriptor(String descriptorUri, MeasurementUnit measurementUnit);

	/**
	 * set a measurement unit as default unit for descriptor
	 * @param descriptorUri the uri of the descriptor
	 * @param unitUri the uri of the measurement unit
	 * @return true if it was successfully added else returns false
	 */
	public boolean setDefaultUnitForDescriptor(String descriptorUri, String unitUri);

	/**
	 * add a namespace to a descriptor
	 * @param descriptorUri the uri of the descriptor
	 * @param namespaceUri the uri of the namespace
	 * @return true if it was successfully added else returns false
	 */
	public boolean addNamespaceToDescriptor(String descriptorUri, String namespaceUri);

	/**
	 * add an existing descriptor to an existing descriptor group
	 * @param groupUri the uri of the descriptor group
	 * @param descriptorUri the uri of the descriptor
	 * @param maxOcurrence max occurrence of the descriptor in the descriptor group
	 * a positive integer value or null for <code>infinite</code> max occurrence
	 * @param mandatory if the descriptor is mandatory in the descriptor group 
	 * @return true if it was successfully added else returns false
	 */
	public boolean addDescriptorToGroup(String groupUri,
			String descriptorUri, Integer maxOcurrence, boolean mandatory);

	/**
	 * add an existing descriptor or descriptor group to an existing category template
	 * @param templateUri the uri of the template
	 * @param categoryUri the uri of the category for the category template
	 * @param descriptorUri the uri of the descriptor or descriptor group
	 * @param maxOcurrence max occurrence of the descriptor or descriptor group in the
	 * category template. A positive integer value or null for <code>infinite</code> max occurrence
	 * @param mandatory if the descriptor or descriptor group is mandatory in the category template
	 * @return true if it was successfully added else returns false
	 */
	public boolean addDescriptorOrGroupToCategoryTemplate(String templateUri,
			String categoryUri, String descriptorUri, Integer maxOcurrence, boolean mandatory);

	/**
	 * sets max occurrence of a descriptor or a descriptor group in a general context
	 * @param descriptorOrGroupUri the uri of the descriptor or the descriptor group
	 * @param maxOccurrence a positive integer value or null for <code>infinite</code> max ocurrence
	 * @return true if it was successfully set else returns false
	 */
	public boolean setMaxOccurrence(String descriptorOrGroupUri, Integer maxOccurrence);

	/**
	 * sets max occurrence of a descriptor in context of a descriptor group
	 * @param groupUri the uri of the descriptor group
	 * @param descriptorUri the uri of the descriptor
	 * @param maxOccurrence a positive integer value or null for <code>infinite</code> max ocurrence
	 * @return true if it was successfully set else returns false
	 */
	public boolean setMaxOccurrenceInGroup(String groupUri, String descriptorUri, Integer maxOccurrence);

	/**
	 * sets max occurrence of a descriptor or a descriptor in
	 * context of a particular category template
	 * @param templateUri the uri of the template for the category template
	 * @param categoryUri the uri of the category for the category template
	 * @param descriptorOrGroupUri the uri of the descriptor or descriptor group
	 * @param maxOccurrence a positive integer value or null for <code>infinite</code> max ocurrence
	 * @return true if it was successfully set else returns false
	 */
	public boolean setMaxOccurrenceInTemplate(String templateUri, String categoryUri, 
			String descriptorOrGroupUri, Integer maxOccurrence);

	/**
	 * sets a descriptor mandatory or optional in a descriptor
	 * @param groupUri the uri of the descriptor group
	 * @param descriptorUri the uri of the descriptor
	 * @param mandatory true if mandatory or false if optional
	 * @return true if the mandatory/optional state was set successfully else returns false
	 */
	public boolean setMandatoryDescriptorInGroup(String groupUri,
			String descriptorUri, boolean mandatory);

	/**
	 * set the descriptor as mandatory or optional in a category template
	 * @param templateUri the uri of the template for the category template
	 * @param categoryUri the uri of the category for the category template
	 * @param descriptorUri the uri of the descriptor
	 * @param mandatory true if mandatory or false if optional
	 * @return true if the mandatory/optional state was set successfully else returns false
	 */
	public boolean setMandatoryDescriptorInCategoryTemplate(String templateUri, String categoryUri, 
			String descriptorUri, boolean mandatory);

	/**
	 * remove descriptor from a descriptor group
	 * @param groupUri the uri of the descriptor group
	 * @param descriptorUri the uri of the descriptor
	 * @return true if it was removed successfully else returns false
	 */
	public boolean removeDescriptorFromGroup(String groupUri, String descriptorUri);

	/**
	 * remove the descriptor or descriptor group from the category template
	 * @param templateUri the uri of the template for the category template
	 * @param categoryUri the uri of the category for the category template
	 * @param descriptorUri the uri of the descriptor
	 * @return true if it was removed successfully else returns false
	 */
	public boolean removeDescriptorOrGroupFromTemplate(String templateUri, String categoryUri,
			String descriptorUri);

	/**
	 * remove the individual from the ontology, remove all statements from the
	 * ontology containing this individual
	 * @param indivUri the uri of the individual
	 * @return true if it was removed successfully else returns false
	 */
	public boolean removeIndividual(String indivUri);

	/**
	 * remove the statement containing the triple
	 * {<code>Subject Property Object</code>}
	 * @param subjectUri the uri of the subject
	 * @param propertyUri the uri of the property
	 * @param objectUri the uri of the object
	 * @return true if it was removed successfully else returns false
	 */
	public boolean removeTriple(String subjectUri, String propertyUri, String objectUri);

	/**
	 * get all measurement units from ontology
	 * @return a list of measurement units (uris are unique)
	 */
	public List<MeasurementUnit> getAllMeasurementUnits();

	/**
	 * saves the current local ontology model to its corresponding ontology file.
	 * In <b>USER_MODE</b> changes in the local ontology is saved in its local ontology file.
	 * In <b>ADMIN_MODE</b> the standard ontology acts as a local ontology and changes in
	 * the standard ontology is saved in its standard ontology file.
	 * @return true if successfully saved else returns false
	 */
	public boolean saveModel();
}
