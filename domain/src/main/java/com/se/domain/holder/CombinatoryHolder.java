package com.se.domain.holder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.se.common.DocHolder;
import com.se.domain.external.Criteria;

import lombok.Data;

import java.util.List;

/**
 * It represent a criteria holder in the external system.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CombinatoryHolder {
    @JsonProperty("criterias")
    private List<Criteria> criteriaList;
    @JsonProperty("docs")
    private List<DocHolder> docs;
}
