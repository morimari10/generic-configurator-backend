package com.se.storage.module;

import com.amazonaws.services.s3.AmazonS3;
import com.se.storage.dao.S3Dao;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for {@link S3Dao}.
 */
@Module
public class S3DaoModule {

    /**
     * Provides an instance of configuration S3 dao implementation.
     *
     * @return the {@link S3Dao} instance.
     */
    @Singleton
    @Provides
    S3Dao provideS3Dao(AmazonS3 s3Client, @Named(AwsS3ConfigurationModule.S3_BUCKET_NAME) String bucketName) {
        return new S3Dao(s3Client, bucketName);
    }
}
