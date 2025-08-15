package com.se.mail.service.template;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import freemarker.cache.ByteArrayTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.se.common.ErrorCode;
import com.se.domain.exception.MailRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * The {@link FreemarkerTemplateService} implementation.
 */
public class FreemarkerTemplateServiceImpl implements FreemarkerTemplateService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Configuration configuration;

    /**
     * Parametrized constructor.
     *
     * @param configuration the freemarker configuration.
     */
    public FreemarkerTemplateServiceImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    @SuppressFBWarnings("SECFREEM")
    public String fillTemplate(String templateFilename, Object dataModel) {
        try {
            Writer writer = new StringWriter();
            Template template = configuration.getTemplate(templateFilename);
            template.process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException exception) {
            ErrorCode errorCode = ErrorCode.ERROR_LOAD_TEMPLATE;
            LOGGER.error("Can't load template file. Message error: [{}], Error code: [{}]",
                    exception.getMessage(),
                    errorCode.getKey());
            throw new MailRuntimeException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }

    @Override
    @SuppressFBWarnings("SECFREEM")
    public String fillTemplate(String templateFilename, InputStream inputStream, Object dataModel) {
        try {
            Writer writer = new StringWriter();
            ByteArrayTemplateLoader byteArrayTemplateLoader = new ByteArrayTemplateLoader();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            byteArrayTemplateLoader.putTemplate(templateFilename, bytes);
            configuration.setTemplateLoader(byteArrayTemplateLoader);
            Template template = configuration.getTemplate(templateFilename);
            LOGGER.debug(template);
            LOGGER.debug(dataModel);

            template.process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException exception) {
            ErrorCode errorCode = ErrorCode.ERROR_LOAD_TEMPLATE;
            LOGGER.error("Can't load template file. Message error: [{}], Error code: [{}]",
                    exception.getMessage(),
                    errorCode.getKey());
            throw new MailRuntimeException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }
}
