package com.se.storage.module;

import dagger.Module;

/**
 * Dagger module that includes all needed storage modules to use in other modules.
 */
@Module(includes = {
        DBConfigurationModule.class,
        MapperModule.class,
        TemplateDaoModule.class,
        ConfigurationDaoModule.class,
        EventDaoModule.class,
        S3ClientModule.class,
        S3DaoModule.class,
})
public class StorageModule {
}
