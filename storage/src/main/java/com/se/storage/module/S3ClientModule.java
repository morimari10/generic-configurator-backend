package com.se.storage.module;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.se.domain.type.ImplementationType;

import dagger.Module;
import dagger.Provides;

import static com.se.storage.module.AwsS3ConfigurationModule.S3_IMPLEMENTATION;
import static com.se.storage.module.AwsS3ConfigurationModule.S3_REGION;
import static com.se.storage.module.AwsS3ConfigurationModule.S3_URL;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for amazon s3 client.
 */
@Module
public class S3ClientModule {

    /**
     * Provides amazon s3 client.
     *
     * @param s3Url            the aws s3 url.
     * @param s3Region         the aws s3Region.
     * @param s3implementation the s3 implementation.
     * @return amazon s3 client.
     */
    @Provides
    @Singleton
    AmazonS3 provideAmazonS3Client(@Named(S3_URL) String s3Url,
                                   @Named(S3_REGION) String s3Region,
                                   @Named(S3_IMPLEMENTATION) ImplementationType s3implementation) {
        switch (s3implementation) {
            case LOCAL:
                return AmazonS3ClientBuilder.standard()
                        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Url, s3Region))
                        .withPathStyleAccessEnabled(Boolean.TRUE)
                        .build();
            case AWS:
                return AmazonS3ClientBuilder.standard().build();
            default:
                throw new IllegalArgumentException("Unsupported implementation type");
        }
    }
}
