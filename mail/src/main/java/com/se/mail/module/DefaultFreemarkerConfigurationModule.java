package com.se.mail.module;

import dagger.Module;
import dagger.Provides;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.inject.Singleton;

import com.se.mail.service.template.FreemarkerTemplateServiceImpl;

/**
 * Dagger freemarker template configuration module.
 * This module provides default configuration for {@link FreemarkerTemplateServiceImpl}
 */
@Module
public class DefaultFreemarkerConfigurationModule {

    private static final String TEMPLATE_PATH = "/template";

    /**
     * Provides template configuration.
     *
     * @return default freemarker template configuration
     */
    @Provides
    @Singleton
    Configuration provideConfiguration() {
        Configuration config = new Configuration(Configuration.VERSION_2_3_20);
        config.setClassForTemplateLoading(FreemarkerTemplateServiceImpl.class, TEMPLATE_PATH);
        config.setDefaultEncoding(StandardCharsets.UTF_8.name());
        config.setLocale(Locale.US);
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return config;
    }
}
