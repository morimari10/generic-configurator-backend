package com.se.handler.module;

import dagger.Component;

import javax.inject.Singleton;

import com.se.handler.MainHandler;
import com.se.service.module.ConfigurationServiceModule;
import com.se.service.module.ConfigurationStepServiceModule;
import com.se.service.module.EventServiceModule;
import com.se.service.module.IntegrationServiceModule;
import com.se.service.module.RestClientModule;
import com.se.service.module.ServicePropertiesModule;
import com.se.service.module.SessionServiceModule;
import com.se.service.module.ValidationPropertiesModule;
import com.se.service.module.ValidatorModule;

/**
 * Dagger application component. Dependency-injected implementation is to be
 * generated from a set of modules.
 * The generated class will have the name of the type annotated with @Component
 * prepended with Dagger.
 */
@Singleton
@Component(modules = {
        SessionServiceModule.class,
        ConfigurationServiceModule.class,
        ServicePropertiesModule.class,
        ActionsModule.class,
        ConfigModule.class,
        HandlerPropertiesModule.class,
        ValidatorModule.class,
        ValidationPropertiesModule.class,
        RestClientModule.class,
        IntegrationServiceModule.class,
        EventServiceModule.class,
        ConfigurationStepServiceModule.class
})
public interface MainComponent {
    /**
     * Inject dagger modules to main handler.
     *
     * @param handler main handler
     */
    void inject(MainHandler handler);
}
