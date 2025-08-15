package com.se.domain.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * It represent a response wrapper.
 *
 * @param <T> the type of payload
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {
    private int code;
    private T payload;
    private Map<String, String> headers;

    /**
     * Constructor.
     *
     * @param code    the code.
     * @param payload the payload.
     */
    public ResponseWrapper(int code, T payload) {
        this.code = code;
        this.payload = payload;
    }
}
