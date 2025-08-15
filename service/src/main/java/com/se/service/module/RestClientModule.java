package com.se.service.module;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for rest client.
 */
@Module
public class RestClientModule {

    public static final String REST_CLIENT_NAME = "restClient";

    /**
     * Provides rest client.
     *
     * @return rest client.
     */
    @Singleton
    @Provides
    @Named(REST_CLIENT_NAME)
    OkHttpClient provideRestClient() {
        return new OkHttpClient();
    }
}
