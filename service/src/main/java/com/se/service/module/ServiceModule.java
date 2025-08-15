package com.se.service.module;

import com.se.storage.module.StorageModule;
import dagger.Module;

/**
 * Dagger module that includes all needed service modules to use in other modules.
 */
@Module(includes = {
        StorageModule.class,
        ServicePropertiesModule.class,
})
public class ServiceModule {
}
