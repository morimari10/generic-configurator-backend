package com.se.storage.module;

import com.se.storage.dao.ConfigurationDBDao;
import com.se.utils.database.ApplicationDynamoDBMapper;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger module for {@link ConfigurationDBDao}.
 */
@Module
public class ConfigurationDaoModule {

    /**
     * Provides an instance of configuration dao implementation.
     *
     * @return the {@link ConfigurationDBDao} instance.
     */
    @Singleton
    @Provides
    ConfigurationDBDao provideConfigurationDao(ApplicationDynamoDBMapper dynamoDBMapper) {
        return new ConfigurationDBDao(dynamoDBMapper);
    }
}
