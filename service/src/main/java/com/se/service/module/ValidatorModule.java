package com.se.service.module;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import static com.se.service.module.RecaptchaPropertiesModule.RECAPTCHA_SECRET_PROPERTY;
import static com.se.service.module.RecaptchaPropertiesModule.RECAPTCHA_VERIFICATION_URL_PROPERTY;

import javax.inject.Named;
import javax.inject.Singleton;

import com.se.service.validator.ConfigurationStatusValidator;
import com.se.service.validator.EmailDataValidator;
import com.se.service.validator.EmailFormFieldsValidator;
import com.se.service.validator.RecaptchaValidator;
import com.se.service.validator.Validator;

/**
 * Dagger module for providing {@link Validator} implementations.
 */
@Module(includes = {RecaptchaPropertiesModule.class, ValidationPropertiesModule.class})
public class ValidatorModule {

        /**
     * Allowed field length property qualifier.
     */
    public static final String FIELD_LENGTH_PATTERN_NAME = "allowedFieldLength";
    /**
     * Allowed email property qualifier.
     */
    public static final String EMAIL_PATTERN_NAME = "allowedEmail";
    /**
     * Allowed message property qualifier.
     */
    public static final String MESSAGE_PATTERN_NAME = "allowedMessage";

    /**
     * Provides a validator for email data.
     *
     * @param emailFormFieldsValidator     form fields validator instance.
     * @param recaptchaValidator           recaptcha validator instance.
     * @param productsLinksValidator       products links validator instance.
     * @param configurationStatusValidator configuration status validator instance.
     * @return {@link EmailDataValidator} instance.
     */
    @Provides
    @Singleton
    EmailDataValidator provideEmailDataValidator(EmailFormFieldsValidator emailFormFieldsValidator,
                                                 RecaptchaValidator recaptchaValidator,
                                                 ConfigurationStatusValidator configurationStatusValidator) {
        return new EmailDataValidator(emailFormFieldsValidator, recaptchaValidator,
                configurationStatusValidator);
    }

     /**
     * Provides a validator for configuration status in email.
     *
     * @return {@link ConfigurationStatusValidator} instance.
     */
    @Provides
    @Singleton
    ConfigurationStatusValidator provideConfigurationStatusValidator() {
        return new ConfigurationStatusValidator();
    }

     /**
     * Provides a validator for email form fields.
     *
     * @param fieldPattern   field pattern
     * @param emailPattern   email pattern
     * @param messagePattern message pattern
     * @return {@link EmailFormFieldsValidator} instance.
     */
    @Provides
    @Singleton
    EmailFormFieldsValidator provideEmailFormFieldsValidator(@Named(FIELD_LENGTH_PATTERN_NAME) String fieldPattern,
                                                             @Named(EMAIL_PATTERN_NAME) String emailPattern,
                                                             @Named(MESSAGE_PATTERN_NAME) String messagePattern) {
        return new EmailFormFieldsValidator(fieldPattern, emailPattern, messagePattern);
    }

     /**
     * Provides a recaptcha validator.
     *
     * @param verificationUrl verification url for recaptcha.
     * @param secretKey       recaptcha secret key.
     * @return {@link RecaptchaValidator} instance.
     */
    @Provides
    @Singleton
    RecaptchaValidator provideCaptchaValidator(@Named(RECAPTCHA_VERIFICATION_URL_PROPERTY) String verificationUrl,
                                               @Named(RECAPTCHA_SECRET_PROPERTY) String secretKey,
                                               @Named(RestClientModule.REST_CLIENT_NAME)
                                                       Lazy<OkHttpClient> restClient) {
        return new RecaptchaValidator(restClient, verificationUrl, secretKey);
    }

}
