package com.se.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * The implementation of {@link S3Client} service layer for interaction with external systems.
 */
public class S3ClientImpl implements S3Client {

    private static final Logger LOGGER = LogManager.getLogger(S3ClientImpl.class);

    private final String bucketName;
    private final AmazonS3 s3Client;

    /**
     * Constructor.
     *
     * @param bucketName bucket name which will be loaded
     * @param s3Client   amazonS3 client
     */
    @Inject
    public S3ClientImpl(String bucketName, AmazonS3 s3Client) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(String fileName, ByteArrayOutputStream file) {
        LOGGER.debug("Uploading file - [{}] to amazon s3", fileName);
        byte[] bytes = file.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        s3Client.putObject(bucketName, fileName, inputStream, metadata);
        LOGGER.info("Filename - [{}]", fileName);
        return fileName;
    }

    @Override
    public InputStream downloadFile(String fileName) {
        LOGGER.debug("Downloading file - [{}] from amazon s3", fileName);
        return s3Client.getObject(bucketName, fileName).getObjectContent();
    }
}
