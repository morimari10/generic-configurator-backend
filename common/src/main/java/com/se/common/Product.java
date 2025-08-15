package com.se.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
@Data
public class Product {
    @JsonProperty("id")
    private String id;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("ean")
    private String ean;
    @JsonProperty("long_description")
    private String longDescription;
    @JsonProperty("short_description")
    private String shortDescription;
}
