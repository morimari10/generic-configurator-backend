package com.se.service.module;

import javax.inject.Singleton;

import com.se.service.session.SessionService;
import com.se.service.session.SessionServiceImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for {@link SessionService}.
 */
@Module
public class SessionServiceModule {

    /**
     * Provides session service instance.
     *
     * @return the {@link SessionService} instance.
     */
    @Singleton
    @Provides
    SessionService provideSessionService() {
        return new SessionServiceImpl();
    }
}
