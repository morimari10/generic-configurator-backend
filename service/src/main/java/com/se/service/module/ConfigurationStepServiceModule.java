package com.se.service.module;

import javax.inject.Singleton;

import com.se.service.configurationStep.ConfigurationStepService;
import com.se.service.configurationStep.ConfigurationStepServiceImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for {@link ConfigurationStepService}.
 */
@Module
public class ConfigurationStepServiceModule {

    /**
     * Provides configuration step service instance.
     *
     * @return the {@link ConfigurationStepService} instance.
     */
    @Singleton
    @Provides
    ConfigurationStepService provideConfigurationStepService() {
        return new ConfigurationStepServiceImpl();
    }
}
