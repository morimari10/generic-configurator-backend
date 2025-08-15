package com.se.domain.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.domain.DBEntity;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * It represents event error in the system.
 */
@Data
@DynamoDBTable(tableName = "error-event")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorEventEntity implements DBEntity {
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "errorCode")
    private String errorCode;
    @DynamoDBAttribute(attributeName = "action")
    private String action;
    @DynamoDBAttribute(attributeName = "parameters")
    private Map<String, String> parameters;
    @DynamoDBAttribute(attributeName = "headers")
    private Map<String, String> headers;
    @DynamoDBAttribute(attributeName = "pathVariables")
    private Map<String, String> pathVariables;
    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "payload")
    private Map<String, Object> payload;
    @DynamoDBAttribute(attributeName = "createdDate")
    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate == null ? null : new Date(createdDate.getTime());
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate == null ? null : new Date(createdDate.getTime());
    }
}
