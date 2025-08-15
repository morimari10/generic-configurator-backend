package com.se.utils;

/**
 * Global constants.
 */
public final class ConfigurationConstants {

    /**
     * S3 bucket name key.
     */
    public static final String S3_BUCKET_NAME_KEY = "s3.bucket.name";

    private ConfigurationConstants() {
        throw new AssertionError("Constructor should not be called directly");
    }
}

