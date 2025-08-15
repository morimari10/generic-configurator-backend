package com.se.storage.module;

import com.se.domain.type.ImplementationType;
import com.se.utils.BasePropertyModule;
import com.se.utils.ConfigurationConstants;
import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for aws clients configurations.
 */
@Module
public class AwsS3ConfigurationModule extends BasePropertyModule {

    /**
     * Amazon S3 images bucket name.
     */
    public static final String S3_BUCKET_NAME = "S3BucketName";
    /**
     * Amazon S3 url.
     */
    public static final String S3_URL = "S3Url";
    /**
     * Amazon S3 region.
     */
    public static final String S3_REGION = "S3Region";
    /**
     * Amazon S3 implementation.
     */
    public static final String S3_IMPLEMENTATION = "S3Implementation";

    private static final String S3_URL_KEY = "s3.url";
    private static final String S3_REGION_KEY = "s3.region";
    private static final String S3_IMPLEMENTATION_KEY = "s3.implementation";


    /**
     * Provides S3 bucket name got from module.
     *
     * @param config the configuration with properties;
     * @return the aws bucket s3 name.
     */
    @Singleton
    @Provides
    @Named(S3_BUCKET_NAME)
    String provideS3BucketName(Config config) {
        return getProperty(config, ConfigurationConstants.S3_BUCKET_NAME_KEY);
    }

    /**
     * Provides S3 url got from module.
     *
     * @param config the configuration with properties;
     * @return the aws s3 url.
     */
    @Singleton
    @Provides
    @Named(S3_URL)
    String provideS3Url(Config config) {
        return getProperty(config, S3_URL_KEY);
    }

    /**
     * Provides S3 region got from module.
     *
     * @param config the configuration with properties;
     * @return the aws s3 region.
     */
    @Singleton
    @Provides
    @Named(S3_REGION)
    String provideS3Region(Config config) {
        return getProperty(config, S3_REGION_KEY);
    }

    /**
     * Provides S3 implementation got from module.
     *
     * @param config the configuration with properties.
     * @return the s3 implementation.
     */
    @Singleton
    @Provides
    @Named(S3_IMPLEMENTATION)
    ImplementationType provideS3Implementation(Config config) {
        return ImplementationType.findByType(getProperty(config, S3_IMPLEMENTATION_KEY));
    }
}
