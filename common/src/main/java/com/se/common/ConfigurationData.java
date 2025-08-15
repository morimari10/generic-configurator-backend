package com.se.common;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class ConfigurationData {
    @JsonProperty("configuration_elements")
    @DynamoDBAttribute(attributeName = "configuration_elements")
    private List<ConfigurationElement> configurationElements;
    @JsonProperty("selection_criteria")
    @DynamoDBAttribute(attributeName = "selection_criteria")
    private Map<String, SelectionCriterias> selectionCriterias;
    @JsonProperty("structure")
    @DynamoDBAttribute(attributeName = "structure")
    private List<Structure> structure;
}
