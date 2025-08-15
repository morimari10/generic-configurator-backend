package com.se.service.module;

import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for recaptcha properties.
 */
@Module
public class RecaptchaPropertiesModule extends PropertiesModule {

    /** Recaptcha verification url property qualifier. */
    public static final String RECAPTCHA_VERIFICATION_URL_PROPERTY = "recaptchaUrl";

    /** Recaptcha secret key property qualifier. */
    public static final String RECAPTCHA_SECRET_PROPERTY = "recaptchaSecret";

    private static final String RECAPTCHA_VERIFICATION_URL_PROPERTY_KEY = "reCaptchaVerificationUrl";
    private static final String RECAPTCHA_SECRET_KEY_PROPERTY_KEY = "reCaptchaSecretKey";

    /**
     * Provides recaptcha verification url got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return recaptcha verification url as string.
     */
    @Singleton
    @Provides
    @Named(RECAPTCHA_VERIFICATION_URL_PROPERTY)
    String provideRecaptchaVerificationUrl(Config config) {
        return getProperty(config, RECAPTCHA_VERIFICATION_URL_PROPERTY_KEY);
    }

    /**
     * Provides recaptcha secret key got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return recaptcha secret key as string.
     */
    @Singleton
    @Provides
    @Named(RECAPTCHA_SECRET_PROPERTY)
    String provideRecaptchaSecretKey(Config config) {
        return getProperty(config, RECAPTCHA_SECRET_KEY_PROPERTY_KEY);
    }
}
