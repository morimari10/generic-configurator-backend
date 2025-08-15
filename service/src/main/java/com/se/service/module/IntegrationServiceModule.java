package com.se.service.module;

import static com.se.service.module.ServicePropertiesModule.SNC_MAIN_GUIDE;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.services.s3.AmazonS3;
import com.se.service.httpClient.HttpClient;
import com.se.service.integration.IntegrationService;
import com.se.service.integration.IntegrationServiceImpl;
import com.se.service.s3.S3Client;
import com.se.service.s3.S3ClientImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for {@link ExportService}.
 */
@Module
public class IntegrationServiceModule {

    /**
     * Provides S3Client impl.
     *
     * @param bucketName     the s3 bucket name
     * @param amazonS3Client the amazon s3 client
     * @return S3Client implementation
     */
    @Singleton
    @Provides
    S3Client provideUploadService(@Named("s3BucketName") String bucketName, AmazonS3 amazonS3Client) {
        return new S3ClientImpl(bucketName, amazonS3Client);
    }

    /**
     * Provides implementation of integration service.
     *
     * @param httpClient      http client
     * @param httpConverter   http converter
     * @param batchDescr      batch descr flag
     */
    @Singleton
    @Provides
    IntegrationService provideIntegrationService(HttpClient httpClient,
        @Named(SNC_MAIN_GUIDE) String sncMainGuide) {
            
        return new IntegrationServiceImpl(httpClient, sncMainGuide, true);
    }
}
