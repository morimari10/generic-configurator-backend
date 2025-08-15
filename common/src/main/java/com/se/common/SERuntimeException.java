package com.se.common;

/**
 * Root of SE runtime exceptions.
 */
public class SERuntimeException extends RuntimeException {

    private final int httpCode;
    private final String errorCode;
    private final String errorTemplate;

    /**
     * Parametrized constructor.
     *
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public SERuntimeException(int httpCode, String errorCode, String errorTemplate) {
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.errorTemplate = errorTemplate;
    }

    /**
     * Parametrized constructor.
     *
     * @param message       the exception message
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error template
     */
    public SERuntimeException(String message, int httpCode, String errorCode, String errorTemplate) {
        super(message);
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.errorTemplate = errorTemplate;
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
    public SERuntimeException(String message, Throwable cause, int httpCode, String errorCode, String errorTemplate) {
        super(message, cause);
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.errorTemplate = errorTemplate;
    }

    /**
     * Parametrized constructor.
     *
     * @param cause         the cause
     * @param httpCode      the http code
     * @param errorCode     the error code
     * @param errorTemplate the error message
     */
    public SERuntimeException(Throwable cause, int httpCode, String errorCode, String errorTemplate) {
        super(cause);
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.errorTemplate = errorTemplate;
    }

    /**
     * Parametrized constructor.
     *
     * @param commonErrorCode the common error code implementation.
     */
    public SERuntimeException(CommonErrorCode commonErrorCode) {
        this.httpCode = commonErrorCode.getHttpCode();
        this.errorCode = commonErrorCode.getKey();
        this.errorTemplate = commonErrorCode.getDescription();
    }

    /**
     * Returns the http code.
     *
     * @return the http code
     */
    public int getHttpCode() {
        return httpCode;
    }

    /**
     * Returns the detail error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the detail error template.
     *
     * @return the error template
     */
    public String getErrorTemplate() {
        return errorTemplate;
    }
}
