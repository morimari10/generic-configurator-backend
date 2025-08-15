package com.se.service.module;

import javax.inject.Singleton;

import com.se.service.configuration.ConfigurationService;
import com.se.service.configuration.ConfigurationServiceImpl;
import com.se.storage.dao.ConfigurationDBDao;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for {@link ConfigurationService}.
 */
@Module
public class ConfigurationServiceModule {

    /**
     * Provides configuration service instance.
     *
     * @return the {@link ConfigurationService} instance.
     */
    @Singleton
    @Provides
    ConfigurationService provideConfigurationService(ConfigurationDBDao configurationDBDao) {
        return new ConfigurationServiceImpl(configurationDBDao);
    }
}
