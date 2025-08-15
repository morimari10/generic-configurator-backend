package com.se.mail.module;

import dagger.Module;
import dagger.Provides;
import freemarker.template.Configuration;

import javax.inject.Singleton;

import com.se.mail.service.template.FreemarkerTemplateService;
import com.se.mail.service.template.FreemarkerTemplateServiceImpl;

/**
 * Dagger module for email templates.
 */
@Module
public class TemplateModule {

    /**
     * Provides an instance of freemarker template service.
     *
     * @param configuration the freemarker configuration.
     * @return the {@link FreemarkerTemplateServiceImpl}.
     */
    @Provides
    @Singleton
    FreemarkerTemplateService provideTemplateService(Configuration configuration) {
        return new FreemarkerTemplateServiceImpl(configuration);
    }
}
