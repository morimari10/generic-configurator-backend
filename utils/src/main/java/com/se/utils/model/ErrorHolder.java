package com.se.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * It represent a error holder in the external system.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorHolder {
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
}
