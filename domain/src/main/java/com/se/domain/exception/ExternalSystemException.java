package com.se.domain.exception;

import com.se.common.CommonErrorCode;
import com.se.common.IntegrationException;

/**
 * External system exception.
 */
public class ExternalSystemException extends IntegrationException {

    /**
     * Parametrized constructor.
     *
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public ExternalSystemException(int httpCode, String errorCode, String errorTemplate) {
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
    public ExternalSystemException(String message, int httpCode, String errorCode, String errorTemplate) {
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
    public ExternalSystemException(String message, Throwable cause, int httpCode, String errorCode,
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
    public ExternalSystemException(Throwable cause, int httpCode, String errorCode, String errorTemplate) {
        super(cause, httpCode, errorCode, errorTemplate);
    }

    /**
     * Parametrized constructor.
     *
     * @param commonErrorCode the common error code implementation.
     */
    public ExternalSystemException(CommonErrorCode commonErrorCode) {
        super(commonErrorCode);
    }
}
