package com.se.mail.module;

import com.se.domain.type.MailClientImplementationType;
import com.se.utils.BasePropertyModule;
import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;
import org.apache.commons.mail.DefaultAuthenticator;

import java.util.Properties;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.mail.Authenticator;

/**
 * Dagger module that provides default properties for sending email.
 */
@Module
public class MailPropertiesModule extends BasePropertyModule {

    /**
     * Mail properties qualifier.
     */
    public static final String MAIL_SERVICE_PROPERTIES_QUALIFIER = "mailProperties";
    /**
     * Mail from qualifier.
     */
    public static final String MAIL_FROM_QUALIFIER = "mailFrom";
    /**
     * Mail client implementation qualifier.
     */
    public static final String MAIL_IMPLEMENTATION_QUALIFIER = "mailImplementation";
    /**
     * Mail amazon region qualifier.
     */
    public static final String MAIL_AMAZON_REGION_QUALIFIER = "mailAmazonRegion";
    /**
     * Mail amazon url qualifier.
     */
    public static final String MAIL_AMAZON_URL_QUALIFIER = "mailAmazonUrl";

    private static final String MAIL_SMTP_HOST_KEY = "mail.smtp.host";
    //private static final String MAIL_SMTP_SOCKET_FACTORY_PORT_KEY = "mail.smtp.socketFactory.port";
    //private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS_KEY = "mail.smtp.socketFactory.class";
    private static final String MAIL_SMTP_AUTH_KEY = "mail.smtp.auth";
    private static final String MAIL_SMTP_START_TTLS_ENABLE_KEY = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";
    private static final String MAIL_SMTP_PORT_KEY = "mail.smtp.port";
    private static final String MAIL_SMTP_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
    private static final String MAIL_USERNAME_KEY = "mail.username";
    private static final String MAIL_PASS_TEST_KEY = "mail.password";
    private static final String MAIL_FROM_KEY = "mail.from";
    private static final String MAIL_IMPLEMENTATION_KEY = "mail.implementation";
    private static final String MAIL_AMAZON_REGION_KEY = "mail.amazon.region";
    private static final String MAIL_AMAZON_URL_KEY = "mail.amazon.url";

    /**
     * Loads properties from {@link Config} and provides them.
     *
     * @param config the {@link Config} instance with properties.
     * @return loaded properties.
     */
    @Provides
    @Singleton
    @Named(MAIL_SERVICE_PROPERTIES_QUALIFIER)
    Properties provideMailProperties(Config config) {
        Properties properties = new Properties();
        properties.setProperty(MAIL_SMTP_HOST_KEY, getProperty(config, MAIL_SMTP_HOST_KEY));
        //properties.setProperty(MAIL_SMTP_SOCKET_FACTORY_PORT_KEY, getProperty(config,
        //MAIL_SMTP_SOCKET_FACTORY_PORT_KEY));
        //properties.setProperty(MAIL_SMTP_SOCKET_FACTORY_CLASS_KEY, getProperty(config,
        //MAIL_SMTP_SOCKET_FACTORY_CLASS_KEY));
        properties.setProperty(MAIL_SMTP_AUTH_KEY, getProperty(config, MAIL_SMTP_AUTH_KEY));
        properties.setProperty(MAIL_SMTP_START_TTLS_ENABLE_KEY, getProperty(config, MAIL_SMTP_START_TTLS_ENABLE_KEY));
        properties.setProperty(MAIL_SMTP_SSL_TRUST, getProperty(config, MAIL_SMTP_SSL_TRUST));
        properties.setProperty(MAIL_SMTP_PORT_KEY, getProperty(config, MAIL_SMTP_PORT_KEY));
        properties.setProperty(MAIL_SMTP_SSL_PROTOCOLS, getProperty(config, MAIL_SMTP_SSL_PROTOCOLS));
        properties.setProperty(MAIL_USERNAME_KEY, getProperty(config, MAIL_USERNAME_KEY));
        properties.setProperty(MAIL_PASS_TEST_KEY, getProperty(config, MAIL_PASS_TEST_KEY));
        properties.setProperty(MAIL_FROM_KEY, getProperty(config, MAIL_FROM_KEY));
        properties.setProperty(MAIL_IMPLEMENTATION_KEY, getProperty(config, MAIL_IMPLEMENTATION_KEY));
        return properties;
    }

    /**
     * Provides mail authenticator.
     *
     * @param config the {@link Config} instance with properties.
     * @return mail authenticator
     */
    @Provides
    @Singleton
    Authenticator provideMailAuthenticator(Config config) {
        return new DefaultAuthenticator(getProperty(config, MAIL_USERNAME_KEY),
                getProperty(config, MAIL_PASS_TEST_KEY));
    }

    /**
     * Provides from mail property.
     *
     * @param config config the {@link Config} instance with properties.
     * @return from mail property
     */
    @Provides
    @Singleton
    @Named(MAIL_FROM_QUALIFIER)
    String provideFromMailProperty(Config config) {
        return getProperty(config, MAIL_FROM_KEY);
    }

    /**
     * Provides mail client implementation property.
     *
     * @param config config config the {@link Config} instance with properties.
     * @return mail client implementation property
     */
    @Provides
    @Singleton
    @Named(MAIL_IMPLEMENTATION_QUALIFIER)
    MailClientImplementationType provideMailServiceImplementation(Config config) {
        return MailClientImplementationType.findByType(getProperty(config, MAIL_IMPLEMENTATION_KEY));
    }

    /**
     * Provides mail amazon region property.
     *
     * @param config config config the {@link Config} instance with properties.
     * @return mail amazon region property
     */
    @Provides
    @Singleton
    @Named(MAIL_AMAZON_REGION_QUALIFIER)
    String provideMailAmazonRegion(Config config) {
        return getProperty(config, MAIL_AMAZON_REGION_KEY);
    }

    /**
     * Provides mail amazon url property.
     *
     * @param config config config the {@link Config} instance with properties.
     * @return mail amazon url property
     */
    @Provides
    @Singleton
    @Named(MAIL_AMAZON_URL_QUALIFIER)
    String provideMailAmazonUrl(Config config) {
        return getProperty(config, MAIL_AMAZON_URL_KEY);
    }
}
