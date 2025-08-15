package com.se.service.module;

import javax.inject.Named;
import javax.inject.Singleton;

import com.typesafe.config.Config;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for services properties.
 */
@Module
public class ValidationPropertiesModule extends PropertiesModule {

    /**
     * Allowed links pattern property qualifier.
     */
    public static final String LINKS_PATTERN_NAME = "allowedLinksPattern";
    /**
     * Allowed config extensions property qualifier.
     */
    public static final String CONFIG_EXTENSIONS_NAME = "allowedConfigurationFileExtensions";
    /**
     * Allowed field length property qualifier.
     */
    public static final String FIELD_LENGTH_PATTERN_NAME = "allowedFieldLength";
    /**
     * Allowed email property qualifier.
     */
    public static final String EMAIL_PATTERN_NAME = "allowedEmail";
    /**
     * Allowed message property qualifier.
     */
    public static final String MESSAGE_PATTERN_NAME = "allowedMessage";

    private static final String VALIDATION_FIELD_LENGTH_PATTERN = "validation.allowedFieldLength.regexp";
    private static final String VALIDATION_EMAIL_PATTERN = "validation.allowedEmail.regexp";
    private static final String VALIDATION_MESSAGE_PATTERN = "validation.allowedMessage.regexp";

    /**
     * Provides allowed field length property from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return allowed field length property.
     */
    @Singleton
    @Provides
    @Named(FIELD_LENGTH_PATTERN_NAME)
    String provideAllowedFileLengthPattern(Config config) {
        return getProperty(config, VALIDATION_FIELD_LENGTH_PATTERN);
    }

    /**
     * Provides allowed email pattern from properties.
     *
     * @param config config the {@link Config} instance with properties.
     * @return allowed email pattern.
     */
    @Singleton
    @Provides
    @Named(EMAIL_PATTERN_NAME)
    String provideAllowedEmailPattern(Config config) {
        return getProperty(config, VALIDATION_EMAIL_PATTERN);
    }

    /**
     * Provides allowed message pattern from properties.
     *
     * @param config config the {@link Config} instance with properties.
     * @return allowed message pattern.
     */
    @Singleton
    @Provides
    @Named(MESSAGE_PATTERN_NAME)
    String provideAllowedMessagePattern(Config config) {
        return getProperty(config, VALIDATION_MESSAGE_PATTERN);
    }


}
