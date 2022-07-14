/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.util;

import java.util.List;

import org.grits.toolbox.editor.samplemanager.ontology.GritsOntologyManagerApi;
import org.grits.toolbox.entry.sample.model.DescriptorGroup;
import org.grits.toolbox.entry.sample.model.Template;
import org.grits.toolbox.entry.sample.utilities.UtilityDescriptorDescriptorGroup;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * 
 *
 */
public class ModelValidator
{
    /**
     * validates model and returns null or appropriate error messages as String 
     * @param ontologyManagerApi the api class - GritsOntologyManagerApi
     * @param localOntologyModel the model to be validated - OntModel
     * @return null if model is valid or else returns error message(s) - String
     */
    public static String validateModel(GritsOntologyManagerApi ontologyManagerApi, OntModel localOntologyModel)
    {
        String message = ModelValidator.validateAllDescriptors(ontologyManagerApi, localOntologyModel);
        message += ModelValidator.validateAllDescriptorGroups(ontologyManagerApi, localOntologyModel);
        message += ModelValidator.validateAllTamplates(ontologyManagerApi, localOntologyModel);
        message = message.isEmpty() ? null : message;
        return message;
    }

    private static String validateAllTamplates(GritsOntologyManagerApi ontologyManagerApi, 
            OntModel localOntologyModel)
    {
        List<Template> templates = ontologyManagerApi.getAllTemplates(localOntologyModel);
        String message = "";
        for(Template template : templates)
        {
            if(UtilityDescriptorDescriptorGroup.getAllDescriptors(template).isEmpty()
                    && UtilityDescriptorDescriptorGroup.getAllDescriptorGroups(template).isEmpty())
            {
                message += "Template : " + template.getLabel() + " is empty.\n";
            }
        }
        return message;
    }

    private static String validateAllDescriptorGroups(GritsOntologyManagerApi ontologyManagerApi, 
            OntModel localOntologyModel)
    {
        List<DescriptorGroup> descriptorGroups = ontologyManagerApi.getAllDescriptorGroups(localOntologyModel);
        String message = "";
        for(DescriptorGroup descriptorGroup : descriptorGroups)
        {
            if(UtilityDescriptorDescriptorGroup.getAllDescriptors(descriptorGroup).isEmpty())
            {
                message += "DescriptorGroup : " + descriptorGroup.getLabel() + " is empty.\n";
            }
        }
        return message;
    }

    private static String validateAllDescriptors(GritsOntologyManagerApi ontologyManagerApi, 
            OntModel localOntologyModel)
    {
        String message = "";

        return message;
    }
}
