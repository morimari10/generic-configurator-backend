package com.se.storage.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.se.common.ErrorCode;
import com.se.common.SERuntimeException;

import java.io.ByteArrayOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dao for interaction with images storage on S3.
 */
public class S3Dao {
    private static final Logger LOGGER = LogManager.getLogger(S3Dao.class);

    private static final String IMAGES_FOLDER = "customer_static/images/";
    private static final String CONFIGURATIONS_FOLDER = "customer_static/configurations/";
    private static final String PREFIX_FORMAT = "customer_static/images/customizations/%s/";
    private static final String JSON_EXTENSION = ".json";

    private AmazonS3 s3Client;
    private String bucketName;

    /**
     * Parametrized constructor.
     *
     * @param s3Client   the amazon s3 client instance.
     * @param bucketName the bucket name.
     */
    public S3Dao(AmazonS3 s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    /**
     * Upload file to {@value IMAGES_FOLDER} S3 folder and removes local copy.
     *
     * @param file         file to upload.
     * @param subDirectory directory which contains uploaded file.
     * @return link on s3.
     */
    public String uploadToImagesFolder(File file, String subDirectory) {
        return upload(file, IMAGES_FOLDER, subDirectory);
    }

    /**
     * Upload file to {@value CONFIGURATIONS_FOLDER} S3 folder and removes local copy.
     *
     * @param file         file to upload.
     * @param subDirectory directory which contains uploaded file.
     * @return link on s3.
     */
    public String uploadToConfigurationsFolder(File file, String subDirectory) {
        return upload(file, CONFIGURATIONS_FOLDER, subDirectory);
    }

    /**
     * Finds specified customization images links on S3 bucket.
     *
     * @param id the customization id.
     * @return list of links.
     * @throws SERuntimeException with data not found error code when there are no images found at all.
     */
    public List<String> findCustomizationLinks(String id) {
        List<String> keys = findKeys(id);
        if (keys.isEmpty()) {
            throw new SERuntimeException(ErrorCode.DATA_NOT_FOUND);
        }
        return keys;
    }

    /**
     * Uploads data to S3.
     *
     * @param name   file name
     * @param folder folder
     * @param data   uploaded data
     */
    public void uploadJsonString(String name, String folder, String data, String contentType) {
        String fileKey = folder + File.separatorChar + name + JSON_EXTENSION;
        LOGGER.debug("Uploading data to S3 the following translation file - [{}]", fileKey);
        byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);
        try (InputStream fileInputStream = new ByteArrayInputStream(dataInBytes)) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            objectMetadata.setContentLength(dataInBytes.length);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, fileInputStream,
                    objectMetadata);
            s3Client.putObject(putObjectRequest);
        } catch (IOException exception) {
            LOGGER.error("An error occurred during uploading the translation - [{}], to the s3 bucket - [{}]",
                    fileKey, bucketName);
            throw new SERuntimeException(ErrorCode.UPLOAD_ERROR);
        }
    }

    /**
     * Downloads file from S3 bucket.
     *
     * @param filePath the file path
     * @return input stream of downloaded file
     */
    public InputStream downloadFile(String filePath) {
        return s3Client.getObject(bucketName, filePath).getObjectContent();
    }

    private List<String> findKeys(String id) {
        String prefix = String.format(PREFIX_FORMAT, id);
        ObjectListing listing = s3Client.listObjects(bucketName, prefix);
        return listing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    public String uploadFile(String fileName, ByteArrayOutputStream file) {
        LOGGER.debug("Uploading file - [{}] to amazon s3", fileName);
        byte[] bytes = file.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        s3Client.putObject(bucketName, fileName, inputStream, metadata);
        return fileName;
    }
    
    public void uploadFileInJsonFormat(String fileName, String data) {
        s3Client.putObject(bucketName, fileName + ".json", data);
        LOGGER.info("##########Bucket Name:" + bucketName
                + " ##########Folder and File name:" + fileName + ".json"
                + "#####URL:" + s3Client.getUrl(bucketName, fileName + ".json").toString());
    }

    private String upload(File file, String folder, String subDirectory) {
        String fileName = file.getName();

        String directory = subDirectory;
        if (!subDirectory.isEmpty() && !subDirectory.endsWith("/")) {
            directory = subDirectory + "/";
        }
        String fileKey = folder + directory + fileName;

        LOGGER.debug("Start uploading file [{}] to [{}]", fileName, fileKey);

        try {
            s3Client.putObject(bucketName, fileKey, file);
            LOGGER.debug("s3dao fileObject [{}] ", file.toString());
        } finally {
            LOGGER.debug("s3dao fileObject finally[{}] ", file.toString());
            removeLocalFile(file);
        }
        return fileKey;
    }

    /**
     * Upload file to {fullPath} S3 folder and removes local copy.
     *
     * @param file         file to upload.
     * @param fullPath     full path directory + file name.
     */
    public void uploadFullPath(File file, String fullPath) {

        LOGGER.debug("Start uploading file [{}]", fullPath);

        try {
            s3Client.putObject(bucketName, fullPath, file);
            LOGGER.debug("s3dao fileObject [{}] ", file.toString());
        } finally {
            LOGGER.debug("s3dao fileObject finally[{}] ", file.toString());
            removeLocalFile(file);
        }
    }

    private void removeLocalFile(File file) {
        String filename = file.getName();
        if (file.delete()) {
            LOGGER.debug("Temporary local file [{}] successfully deleted.", filename);
        } else {
            LOGGER.error("Temporary local file [{}] wasn't deleted.", filename);
        }
    }
}
