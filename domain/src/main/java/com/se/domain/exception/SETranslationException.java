package com.se.domain.exception;

/**
 * Runtime exception for se-translation.
 */
public class SETranslationException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param cause cause exception
     */
    public SETranslationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message exception message
     */
    public SETranslationException(String message) {
        super(message);
    }
}
