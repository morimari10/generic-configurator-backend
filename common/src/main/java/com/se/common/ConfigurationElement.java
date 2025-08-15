package com.se.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ConfigurationElement {
    @DynamoDBAttribute(attributeName = "id")
    @JsonProperty("id")
    private String id;
    @DynamoDBAttribute(attributeName = "zone")
    @JsonProperty("zone")
    private String zone;
    @DynamoDBAttribute(attributeName = "globaleZone")
    @JsonProperty("globaleZone")
    public String globaleZone;
    @DynamoDBAttribute(attributeName = "productType")
    @JsonProperty("productType")
    private String productType;
    @DynamoDBAttribute(attributeName = "catalog")
    @JsonProperty("catalog")
    private String catalog;
    @DynamoDBAttribute(attributeName = "device")
    @JsonProperty("device")
    private String device;
    @DynamoDBAttribute(attributeName = "quantity")
    @JsonProperty("quantity")
    private String quantity;
    @DynamoDBAttribute(attributeName = "isDefault")
    @JsonProperty("isDefault")
    private Boolean isDefault;

    public ConfigurationElement(String zone, String globaleZone, String productType, String catalog, String device) {
        this.zone = zone;
        this.globaleZone = globaleZone;
        this.productType = productType;
        this.catalog = catalog;
        this.device = device;
    }

    public ConfigurationElement(String id, String zone, String globaleZone, String productType, String catalog,
            String device) {
        this.id = id;
        this.zone = zone;
        this.globaleZone = globaleZone;
        this.productType = productType;
        this.catalog = catalog;
        this.device = device;
    }

}
