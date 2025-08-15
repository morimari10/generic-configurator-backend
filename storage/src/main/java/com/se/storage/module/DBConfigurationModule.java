package com.se.storage.module;

import com.se.domain.type.ImplementationType;
import com.se.utils.BasePropertyModule;
import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for aws clients configurations.
 */
@Module
public class DBConfigurationModule extends BasePropertyModule {

    /**
     * Database URL.
     */
    public static final String DATABASE_CONFIGURATION_URL = "databaseConfigurationUrl";

    /**
     * Database region.
     */
    public static final String DATABASE_CONFIGURATION_REGION = "databaseConfigurationRegion";

    /**
     * Database tables prefix.
     */
    public static final String DATABASE_CONFIGURATION_PREFIX = "databaseConfigurationPrefix";

    /**
     * Database tables postfix..
     */
    public static final String DATABASE_CONFIGURATION_POSTFIX = "databaseConfigurationPostfix";

    /**
     * Database type implementation.
     */
    public static final String DATABASE_CONFIGURATION_IMPL = "databaseConfigurationImplementation";

    private static final String DATABASE_CONFIGURATION_URL_KEY = "database.configuration.url";
    private static final String DATABASE_CONFIGURATION_REGION_KEY = "database.configuration.region";
    private static final String DATABASE_CONFIGURATION_PREFIX_KEY = "database.configuration.prefix";
    private static final String DATABASE_CONFIGURATION_POSTFIX_KEY = "database.configuration.postfix";
    private static final String DATABASE_CONFIGURATION_IMPL_KEY = "database.configuration.implementation";

    /**
     * Provides dynamo db url got from module.
     *
     * @param config the configuration with properties;
     * @return the url string.
     */
    @Singleton
    @Provides
    @Named(DATABASE_CONFIGURATION_URL)
    String provideDynamoDbUrl(Config config) {
        return getProperty(config, DATABASE_CONFIGURATION_URL_KEY);
    }

    /**
     * Provides dynamo db region got from module.
     *
     * @param config the configuration with properties;
     * @return the region string.
     */
    @Singleton
    @Provides
    @Named(DATABASE_CONFIGURATION_REGION)
    String provideDynamoDbRegion(Config config) {
        return getProperty(config, DATABASE_CONFIGURATION_REGION_KEY);
    }

    /**
     * Provides dynamo db prefix got from module.
     *
     * @param config the configuration with properties;
     * @return the prefix string.
     */
    @Singleton
    @Provides
    @Named(DATABASE_CONFIGURATION_PREFIX)
    String provideDynamoDbPrefix(Config config) {
        return getProperty(config, DATABASE_CONFIGURATION_PREFIX_KEY);
    }

    /**
     * Provides dynamo db postfix got from module.
     *
     * @param config the configuration with properties;
     * @return the prefix string.
     */
    @Singleton
    @Provides
    @Named(DATABASE_CONFIGURATION_POSTFIX)
    String provideDynamoDbPostfix(Config config) {
        return getProperty(config, DATABASE_CONFIGURATION_POSTFIX_KEY);
    }

    /**
     * Provides dynamo db implementation property got from module.
     *
     * @param config the configuration with properties;
     * @return the implementation property value.
     */
    @Singleton
    @Provides
    @Named(DATABASE_CONFIGURATION_IMPL)
    ImplementationType provideDynamoDbImplementation(Config config) {
        return ImplementationType.findByType(getProperty(config, DATABASE_CONFIGURATION_IMPL_KEY));
    }
}
