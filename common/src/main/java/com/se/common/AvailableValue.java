package com.se.common;

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
public class AvailableValue {
    @JsonProperty("value")
    @DynamoDBAttribute(attributeName = "value")
    private String value;
    @JsonProperty("active")
    @DynamoDBAttribute(attributeName = "active")
    private boolean active;
    @JsonProperty("available")
    @DynamoDBAttribute(attributeName = "available")
    private boolean available;


}
