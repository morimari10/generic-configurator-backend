package com.se.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class SelectionCriterias {
    @JsonProperty("selectionCriteria")
    @DynamoDBAttribute(attributeName = "selectionCriteria")
    private List<SelectionCriteria> selectionCriteria;
    @JsonProperty("mainSelection")
    @DynamoDBAttribute(attributeName = "mainSelection")
    private List<String> mainSelection;
    @JsonProperty("globalRanking")
    @DynamoDBAttribute(attributeName = "globalRanking")
    private String globalRanking;


}
