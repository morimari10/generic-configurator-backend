package com.se.service.module;

import com.se.service.event.EventService;
import com.se.storage.dao.EventDao;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger module for {@link EventService}.
 */
@Module
public class EventServiceModule {

    /**
     * Provides event service instance.
     *
     * @param eventDao the event dao instance.
     * @return the {@link EventService} instance.
     */
    @Singleton
    @Provides
    EventService provideEventService(EventDao eventDao) {
        return new EventService(eventDao);
    }
}
