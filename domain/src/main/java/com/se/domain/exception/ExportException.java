package com.se.domain.exception;

import com.se.common.SERuntimeException;

/**
 * Exception thrown when a DaggerModule encounters an error when attempting to create a dagger module.
 */
public class ExportException extends SERuntimeException {

    /**
     * Parametrized constructor.
     *
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public ExportException(int httpCode, String errorCode, String errorTemplate) {
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
    public ExportException(String message, int httpCode, String errorCode, String errorTemplate) {
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
    public ExportException(String message, Throwable cause, int httpCode, String errorCode, String errorTemplate) {
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
    public ExportException(Throwable cause, int httpCode, String errorCode, String errorTemplate) {
        super(cause, httpCode, errorCode, errorTemplate);
    }
}
