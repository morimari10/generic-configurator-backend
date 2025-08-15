package com.se.common;

/**
 * Common interface for error codes.
 */
public interface CommonErrorCode {

    /**
     * Method for getting an error short key.
     *
     * @return error short key.
     */
    String getKey();

    /**
     * Method for getting an error description.
     *
     * @return error description.
     */
    String getDescription();

    /**
     * Method for getting an http error code for corresponding error.
     *
     * @return http error code.
     */
    int getHttpCode();
}
