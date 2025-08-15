package com.se.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
@Data
public abstract class StepSetup {
    private String title;
    private String uiComponent;
    private Integer rank;
    private String type;
    private String format;
    private Boolean mandatory;
    private String criteria;
}
