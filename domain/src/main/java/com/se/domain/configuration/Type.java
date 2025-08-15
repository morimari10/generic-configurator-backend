package com.se.domain.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Type {
    @JsonProperty("value")
    @DynamoDBAttribute(attributeName = "value")
    String value;

    @JsonCreator
    public Type( @JsonProperty("value") String value) {
        this.value = value;
    }
}
