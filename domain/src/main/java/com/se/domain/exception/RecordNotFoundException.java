package com.se.domain.exception;

import com.se.common.CommonErrorCode;

/**
 * Record not found exception.
 */
public class RecordNotFoundException extends DatabaseException {

    /**
     * Parametrized constructor.
     *
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public RecordNotFoundException(int httpCode, String errorCode, String errorTemplate) {
        super(httpCode, errorCode, errorTemplate);
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
    public RecordNotFoundException(String message, Throwable cause, int httpCode, String errorCode,
                                   String errorTemplate) {
        super(message, cause, httpCode, errorCode, errorTemplate);
    }

    /**
     * Parametrized constructor.
     *
     * @param commonErrorCode the common error code implementation.
     */
    public RecordNotFoundException(CommonErrorCode commonErrorCode) {
        super(commonErrorCode);
    }
}
