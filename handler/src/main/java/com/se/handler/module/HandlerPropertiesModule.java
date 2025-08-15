package com.se.handler.module;

import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;

import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for handler properties.
 */
@Module
public class HandlerPropertiesModule extends com.se.service.module.PropertiesModule {

    /** Allowed origins property qualifier. */
    public static final String ALLOWED_ORIGINS_PROPERTY = "allowedOrigins";


    /**
     * Provides allowed origins from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return allowed origins as list of strings.
     */
    @Singleton
    @Provides
    @Named(ALLOWED_ORIGINS_PROPERTY)
    List<String> provideAllowedOrigins(Config config) {
        return getProperties(config, ALLOWED_ORIGINS_PROPERTY);
    }
}
