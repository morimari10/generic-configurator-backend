package com.se.common;

import java.util.List;

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
public class SelectionCriteria {
    @JsonProperty("productType")
    @DynamoDBAttribute(attributeName = "productType")
    private String productType;
    @JsonProperty("criteria")
    @DynamoDBAttribute(attributeName = "criteria")
    private String criteria;
    @JsonProperty("label")
    @DynamoDBAttribute(attributeName = "label")
    private String label;
    @JsonProperty("ranking")
    @DynamoDBAttribute(attributeName = "ranking")
    private String ranking;
    @JsonProperty("uiComponent")
    @DynamoDBAttribute(attributeName = "uiComponent")
    private String uiComponent;
    @JsonProperty("availableValues")
    @DynamoDBAttribute(attributeName = "availableValues")
    private List<AvailableValue> availableValues;
    @JsonProperty("selectedValue")
    @DynamoDBAttribute(attributeName = "selectedValue")
    private String selectedValue;
    @JsonProperty("infoTip")
    @DynamoDBAttribute(attributeName = "infoTip")
    private String infoTip;
    @JsonProperty("defaultValue")
    @DynamoDBAttribute(attributeName = "defaultValue")
    private String defaultValue;
    @JsonProperty("infoTipPerAvailableValue")
    @DynamoDBAttribute(attributeName = "infoTipPerAvailableValue")
    private Boolean infoTipPerAvailableValue;

    public SelectionCriteria(String productType, String criteria, String label, String ranking, String uiComponent) {
        this.productType = productType;
        this.criteria = criteria;
        this.label = label;
        this.ranking = ranking;
        this.uiComponent = uiComponent;
    }

    public SelectionCriteria(String productType, String criteria, String label, String ranking, String uiComponent,
            List<AvailableValue> availableValues) {
        this.productType = productType;
        this.criteria = criteria;
        this.label = label;
        this.ranking = ranking;
        this.uiComponent = uiComponent;
        this.availableValues = availableValues;
    }

}
