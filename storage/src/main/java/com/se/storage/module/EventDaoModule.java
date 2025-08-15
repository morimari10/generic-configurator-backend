package com.se.storage.module;

import com.se.storage.dao.EventDao;
import com.se.utils.database.ApplicationDynamoDBMapper;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger module for {@link EventDao}.
 */
@Module
public class EventDaoModule {

    /**
     * Provides event dao implemented with dynamo db mapper.
     *
     * @return the {@link EventDao} instance.
     */
    @Singleton
    @Provides
    EventDao provideEventDao(ApplicationDynamoDBMapper dynamoDBMapper) {
        return new EventDao(dynamoDBMapper);
    }
}
