package com.se.common;

import org.apache.http.HttpStatus;

/**
 * Harmony+ error codes. Contains short keys, descriptions and http error codes
 * (500 if not specified).
 */
public enum ErrorCode implements CommonErrorCode {

        /**
         * Export error.
         */
        EXPORT_ERROR("EXPORT-ERR-001", "export error", HttpStatus.SC_INTERNAL_SERVER_ERROR),

        /**
         * Id isn't specified error.
         */
        NO_ID("ERR-NO_ID", "Id isn't specified.", HttpStatus.SC_BAD_REQUEST),
        /**
         * External system unavailable.
         */
        EXTERNAL_SYSTEM_UNAVAILABLE("ERR-002", "Error occurs in interaction with external system",
                        HttpStatus.SC_INTERNAL_SERVER_ERROR),
        /**
         * Application config initialize fail.
         */
        APPLICATION_CONFIG_INIT_FAIL("ERR-003", "Error occurs on application configuration",
                        HttpStatus.SC_INTERNAL_SERVER_ERROR),

        /**
         * Error for missing action for provided endpoint.
         */
        NO_SUCH_ENDPOINT("ERR-NO_SUCH_ENDPOINT", "No such endpoint found", HttpStatus.SC_NOT_FOUND),

        /**
         * Invalid request body error.
         */
        INVALID_REQUEST("ERR-INVALID_REQUEST", "The request body isn't valid", HttpStatus.SC_BAD_REQUEST),

        /**
         * Invalid file extension error.
         */
        INVALID_FILE_EXTENSION("ERR-INVALID_FILE_EXTENSION", "Uploaded file has invalid extension",
                        HttpStatus.SC_BAD_REQUEST),

        /**
         * Invalid file size error.
         */
        INVALID_FILE_SIZE("ERR-INVALID_FILE_SIZE", "Uploaded file is too big", HttpStatus.SC_BAD_REQUEST),

        /**
         * Error any non-described error.
         */
        UNKNOWN_ERROR("ERR-UNKNOWN", "Occurred an unknown error", Constants.UNKNOWN_ERROR_CODE),

        /**
         * Invalid configuration file error.
         */
        INVALID_CONFIGURATION_FILE("ERR-INVALID_CONFIGURATION_FILE", "The configuration file isn't valid",
                        HttpStatus.SC_BAD_REQUEST),

        /**
         * Invalid recaptcha response error.
         */
        INVALID_RECAPTCHA_RESPONSE("ERR-INVALID_RECAPTCHA_RESPONSE", "Recaptcha validation failed",
                        HttpStatus.SC_BAD_REQUEST),

        /**
         * Unsupported configuration status.
         */
        UNSUPPORTED_CONFIG_STATUS("ERR-UNSUPPORTED_CONFIGURATION_STATUS", "Unsupported configuration status",
                        HttpStatus.SC_BAD_REQUEST),

        /**
         * File generation error.
         */
        FILE_GENERATION_ERROR("ERROR-FILE_GENERATION", "Occurred an error during file generation"),

        /**
         * Image transcoding error.
         */
        IMAGE_TRANSCODING_ERROR("ERROR-IMAGE_TRANSCODING", "Occurred an error during transcoding process"),

        /**
         * Temp file writing error.
         */
        TEPM_FILE_WRITING_ERROR("ERROR-TEMP_FILE_WRITING", "Occurred an error during writing temp file"),

        /**
         * Connectivity error.
         */
        CONNECTIVITY_ERROR("ERROR-CONNECTIVITY", "Occurred an error during connection to external lambda"),
        /**
         * Template is not available error.
         */
        TEMPLATE_IS_NOT_AVAILABLE_ERROR("ERROR-TEMPLATE_NOT_AVAILABLE",
                        "Template for this product is not available",
                        HttpStatus.SC_NOT_FOUND),
        /**
         * External data inconstancy.
         */
        EXTERNAL_DATA_INCONSTANCY("ERR-004", "Error occurs on parsing external data",
                        HttpStatus.SC_INTERNAL_SERVER_ERROR),
        /**
         * Data access fail.
         */
        DATA_ACCESS_FAIL("SE-ERR-DATA_ACCESS_FAIL", "Data access fail", HttpStatus.SC_INTERNAL_SERVER_ERROR),
        /**
         * Data not found error.
         */
        DATA_NOT_FOUND("SE-ERR-DATA_NOT_FOUND", "Data not found", HttpStatus.SC_NOT_FOUND),
        /**
         * Error sending mail.
         */
        MAIL_SEND_FAIL("MAIL-ERROR-SEND_FAIL", "Error sending mail", HttpStatus.SC_INTERNAL_SERVER_ERROR),

        /**
         * Error load template.
         */
        ERROR_LOAD_TEMPLATE("MAIL-ERROR-LOAD_TEMPLATE_FAIL", "Error load template",
                        HttpStatus.SC_INTERNAL_SERVER_ERROR),
        /**
         * Error during image converting.
         */
        ERROR_DURING_IMAGE_CONVERT("ERROR-IMAGE-CONVERTING", "Error image converting",
                        HttpStatus.SC_INTERNAL_SERVER_ERROR),

        /**
         * Error during adding certificate to system key storage.
         */
        ERROR_DURING_ADDING_SDL_CERTIFICATE("ERROR-CERTIFICATE-ADDING",
                        "Error adding sdl certificate to system key storage", HttpStatus.SC_INTERNAL_SERVER_ERROR),

        /**
         * Error during call captcha service.
         */
        ERROR_CAPTCHA_SERVICE_UNAVAILABLE("ERROR-CAPTCHA-SERVICE-UNAVAILABLE",
                        "ReCaptcha service is unavailable",
                        HttpStatus.SC_INTERNAL_SERVER_ERROR),
        /**
         * Error during uploading files to s3.
         */
        UPLOAD_ERROR("ERROR-UPLOAD-TO-S3", "Error occurred during uploading to S3",
                        HttpStatus.SC_INTERNAL_SERVER_ERROR),

        /**
         * Bad request error.
         */
        BAD_REQUEST("SE-BAD-REQUEST", "Server could not understand the request due to invalid syntax",
                        HttpStatus.SC_BAD_REQUEST),

        /**
         * Error during session validation.
         */
        SESSION_IS_NOT_VALID("SESSION-VALIDATION", "Session-Id is not valid",
                        HttpStatus.SC_BAD_REQUEST);


        private String key;
        private String description;
        private int httpCode;

        /**
         * Constructor.
         *
         * @param key         key of exception.
         * @param description short description.
         */
        ErrorCode(String key, String description) {
                this(key, description, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        /**
         * Constructor.
         *
         * @param key         key of exception.
         * @param description short description.
         * @param httpCode    http error code.
         */
        ErrorCode(String key, String description, int httpCode) {
                this.key = key;
                this.description = description;
                this.httpCode = httpCode;
        }

        @Override
        public String getKey() {
                return key;
        }

        @Override
        public String getDescription() {
                return description;
        }

        @Override
        public int getHttpCode() {
                return httpCode;
        }

        private static class Constants {
                static final int UNKNOWN_ERROR_CODE = 520;
        }
}