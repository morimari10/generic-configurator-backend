package com.se.handler.module;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dagger.Module;
import dagger.Provides;

import java.util.Optional;

import javax.inject.Singleton;

/**
 * Dagger module module provide configuration factory.
 */
@Module
public class ConfigModule {

    private static final String PROPS_FILE_NAME_TEMPLATE = "application-%s.properties";
    private static final String DEFAULT_PROPS_FILE_NAME = "application.properties";

    /**
     * Provides module object.
     *
     * @return module object
     */
    @Provides
    @Singleton
    Config provideConfig() {
        return ConfigFactory.parseResources(Optional.ofNullable(System.getenv("env"))
                .map(value -> String.format(PROPS_FILE_NAME_TEMPLATE, value))
                .orElse(DEFAULT_PROPS_FILE_NAME));
    }
}
