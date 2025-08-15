package com.se.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.se.common.DocHolder;

import lombok.Data;

import java.util.List;

/**
 * It represent a criteria holder in the external system.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CriteriaHolder {
    @JsonProperty("criterias")
    private List<Criteria> criteriaList;
    @JsonProperty("docs")
    private List<DocHolder> docs;
}
