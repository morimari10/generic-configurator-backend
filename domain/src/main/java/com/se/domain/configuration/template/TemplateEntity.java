package com.se.domain.configuration.template;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.se.domain.DBEntity;
import com.se.domain.configuration.converter.MapToNodeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Domain object that represents template entity in database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDBTable(tableName = "template")
public class TemplateEntity implements DBEntity {

    /**
     * DynamoDB index name of session id column.
     */
    public static final String TEMPLATE_TYPE_INDEX = "Template-Type-Index";

    @DynamoDBHashKey
    private String cr;

    @DynamoDBAttribute(attributeName = "expressDelivery")
    private Boolean expressDelivery;

    @DynamoDBTypeConverted(converter = MapToNodeConverter.class)
    @JsonPropertyOrder({"id", "type", "definitions", "areas"})
    private JsonNode template;

    @DynamoDBAttribute
    private List<StandardCustomization> standardCustomizations = Collections.emptyList();

    @DynamoDBAttribute
    private String gcr;

    @DynamoDBAttribute(attributeName = "preview")
    private TemplatePreview preview;

    @DynamoDBAttribute(attributeName = "templateType")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = TEMPLATE_TYPE_INDEX)
    private String templateType;

    @DynamoDBAttribute(attributeName = "size")
    private String size;

    @DynamoDBAttribute(attributeName = "metadata")
    private Metadata metadata;

    @DynamoDBAttribute(attributeName = "disabledTemplate")
    private Boolean disabledTemplate;

    @DynamoDBAttribute(attributeName = "uploadingDate")
    private String uploadingDate;

    /**
     * Parametrized constructor.
     *
     * @param cr commercial reference for which the template is.
     */
    public TemplateEntity(String cr) {
        this.cr = cr;
    }

    public Boolean getExpressDelivery() {
        return Optional.ofNullable(expressDelivery).orElse(Boolean.FALSE);
    }

}
