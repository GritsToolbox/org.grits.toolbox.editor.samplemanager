/**
 * 
 */
package org.grits.toolbox.editor.samplemanager.input;

import org.grits.toolbox.entry.sample.model.Template;


/**
 * 
 *
 */
public class TemplateWithFeatures extends ClassesWithFeatures
{

    private Template template = null;

    public TemplateWithFeatures(Template template)
    {
        this.setTemplate(template);
    }

    @Override
    public String getUri()
    {
        return this.template.getUri();
    }

    @Override
    public String getLabel()
    {
        return this.template.getLabel();
    }

    @Override
    public void setLabel(String label)
    {
        this.template.setLabel(label);
    }

    @Override
    public String getDescription()
    {
        return this.template.getDescription();
    }

    @Override
    public void setDescription(String description)
    {
        this.template.setDescription(description);
    }

    /**
     * @return the template
     */
    public Template getTemplate()
    {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(Template template)
    {
        this.template = template;
    }

}
