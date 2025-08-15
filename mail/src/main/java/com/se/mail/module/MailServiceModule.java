package com.se.mail.module;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.se.domain.type.MailClientImplementationType;
import com.se.mail.service.AmazonMailClient;
import com.se.mail.service.ApacheMailClient;
import com.se.mail.service.MailService;

import dagger.Module;
import dagger.Provides;

import java.util.Properties;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.mail.Authenticator;
import javax.mail.Session;

/**
 * Dagger module that provides mail service.
 */
@Module(includes = {DefaultFreemarkerConfigurationModule.class, MailPropertiesModule.class, TemplateModule.class})
public class MailServiceModule {

    /**
     * Provides an implementation of {@link MailService} based on properties.
     *
     * @param properties     mail properties.
     * @param mailFrom       mail from.
     * @param implementation mail implementation.
     * @param region         aws region.
     * @param authenticator  mail authenticator.
     * @return the implementation of {@link MailService} based on mail.implementation property.
     */
    @Singleton
    @Provides
    MailService provideMailService(@Named(MailPropertiesModule.MAIL_SERVICE_PROPERTIES_QUALIFIER) Properties properties,
                                   @Named(MailPropertiesModule.MAIL_FROM_QUALIFIER) String mailFrom,
                                   @Named(MailPropertiesModule.MAIL_IMPLEMENTATION_QUALIFIER)
                                           MailClientImplementationType implementation,
                                   @Named(MailPropertiesModule.MAIL_AMAZON_REGION_QUALIFIER) String region,
                                   @Named(MailPropertiesModule.MAIL_AMAZON_URL_QUALIFIER) String url,
                                   Authenticator authenticator) {
        switch (implementation) {
            case AWS:
                return new AmazonMailClient(AmazonSimpleEmailServiceClientBuilder.standard()
                        .withRegion(Regions.fromName(region))
                        .build(), mailFrom);
            case APACHE:
                return new ApacheMailClient(Session.getInstance(properties, authenticator), mailFrom);
            case LOCAL:
                return new AmazonMailClient(AmazonSimpleEmailServiceClientBuilder.standard()
                        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(url, region))
                        .build(), mailFrom);
            default:
                throw new IllegalArgumentException("Unsupported implementation type");
        }
    }
}
