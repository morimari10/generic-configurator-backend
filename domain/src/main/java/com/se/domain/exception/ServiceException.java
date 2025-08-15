package com.se.domain.exception;

import java.util.Arrays;
import java.util.List;

import com.se.common.CommonErrorCode;
import com.se.common.SERuntimeException;

/**
 * Service exceptions.
 */
public class ServiceException extends SERuntimeException {

    private final List<String> values;

    public List<String> getValues() {
        return values;
    }

    /**
     * Parametrized constructor.
     *
     * @param errorCode the error code.
     * @param values    values for message.
     */
    public ServiceException(CommonErrorCode errorCode, String... values) {
        super(errorCode);
        this.values = Arrays.asList(values);
    }
}
