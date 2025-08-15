package com.se.common;

/**
 * External data exception.
 */
public class ExternalDataException extends IntegrationException {

    /**
     * Parametrized constructor.
     *
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public ExternalDataException(int httpCode, String errorCode, String errorTemplate) {
        super(httpCode, errorCode, errorTemplate);
    }

    /**
     * Parametrized constructor.
     *
     * @param message       the exception message
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public ExternalDataException(String message, int httpCode, String errorCode, String errorTemplate) {
        super(message, httpCode, errorCode, errorTemplate);
    }

    /**
     * Parametrized constructor.
     *
     * @param message       the exception message
     * @param cause         the cause
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error message
     */
    public ExternalDataException(String message, Throwable cause, int httpCode, String errorCode,
                                 String errorTemplate) {
        super(message, cause, httpCode, errorCode, errorTemplate);
    }

    /**
     * Parametrized constructor.
     *
     * @param cause         the cause
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error message
     */
    public ExternalDataException(Throwable cause, int httpCode, String errorCode, String errorTemplate) {
        super(cause, httpCode, errorCode, errorTemplate);
    }

    /**
     * Parametrized constructor.
     *
     * @param commonErrorCode the common error code implementation.
     */
    public ExternalDataException(CommonErrorCode commonErrorCode) {
        super(commonErrorCode);
    }
}
