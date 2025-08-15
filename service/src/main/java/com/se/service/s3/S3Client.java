package com.se.service.s3;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * The service layer for upload file to AmazonS3.
 */
public interface S3Client {

    /**
     * Upload file to s3 bucket.
     *
     * @param fileName file name.
     * @param file     file to upload.
     * @return link
     */
    String uploadFile(String fileName, ByteArrayOutputStream file);

    /**
     * Download file from s3 bucket.
     *
     * @param fileName the file name
     * @return stream of file
     */
    InputStream downloadFile(String fileName);
}
