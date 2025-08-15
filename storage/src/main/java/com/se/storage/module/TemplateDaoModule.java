package com.se.storage.module;

import com.se.storage.dao.TemplateDao;
import com.se.utils.database.ApplicationDynamoDBMapper;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger module for {@link TemplateDao}.
 */
@Module
public class TemplateDaoModule {

    /**
     * Provides template dao implemented with dynamo db.
     *
     * @return the {@link TemplateDao} instance.
     */
    @Singleton
    @Provides
    TemplateDao provideTemplateDao(ApplicationDynamoDBMapper dynamoDBMapper) {
        return new TemplateDao(dynamoDBMapper);
    }
}
