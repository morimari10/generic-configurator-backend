package com.se.mail.service.template;

import java.io.InputStream;

/**
 * Service for working with freemarker templates.
 */
public interface FreemarkerTemplateService {

    /**
     * Fills a template from specified file with specified model.
     *
     * @param templateFilename the template file name.
     * @param dataModel        the data model for mapping.
     * @return filled template.
     */
    String fillTemplate(String templateFilename, Object dataModel);

    /**
     * Fills a template from input stream.
     *
     * @param templateFilename the template file name.
     * @param inputStream      the input stream of template
     * @param dataModel        data model
     * @return filled template
     */
    String fillTemplate(String templateFilename, InputStream inputStream, Object dataModel);
}
