package com.se.common;

/**
 * Integration exception.
 */
public class IntegrationException extends SERuntimeException {

    /**
     * Parametrized constructor.
     *
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public IntegrationException(int httpCode, String errorCode, String errorTemplate) {
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
    public IntegrationException(String message, int httpCode, String errorCode, String errorTemplate) {
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
    public IntegrationException(String message, Throwable cause, int httpCode, String errorCode, String errorTemplate) {
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
    public IntegrationException(Throwable cause, int httpCode, String errorCode, String errorTemplate) {
        super(cause, httpCode, errorCode, errorTemplate);
    }

    /**
     * Parametrized constructor.
     *
     * @param commonErrorCode the common error code implementation.
     */
    public IntegrationException(CommonErrorCode commonErrorCode) {
        super(commonErrorCode);
    }
}
