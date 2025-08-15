package com.se.domain.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * It represent a criteria in the external system.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Criteria {
    @JsonProperty("criteriaId")
    private String id;
    @JsonProperty("values")
    private List<String> values;
    @JsonProperty("isAssigned")
    private Boolean isAssigned;
}
