package com.se.domain.exception;

import com.se.common.SERuntimeException;

public class ConfigCreationException extends SERuntimeException {

    /**
     * Parametrized constructor.
     *
     * @param message       the exception message
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public ConfigCreationException(String message, int httpCode, String errorCode, String errorTemplate) {
        super(message, httpCode, errorCode, errorTemplate);
    }
    
}
