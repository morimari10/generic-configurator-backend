package com.se.domain.configuration.event;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.se.domain.DBEntity;
import com.se.domain.configuration.converter.MapToNodeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Domain object that represents event data in the database.
 */
@Data
@DynamoDBTable(tableName = "event")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class EventData implements DBEntity {

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String configurationId;

    @DynamoDBAttribute
    private Date createdDate;

    @DynamoDBAttribute
    private String tag;

    @DynamoDBAttribute
    private String locale;

    @DynamoDBAttribute
    private String eventType;

    @DynamoDBTypeConverted(converter = MapToNodeConverter.class)
    private JsonNode metaInfo;

    public Date getCreatedDate() {
        return cloneDate(createdDate);
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = cloneDate(createdDate);
    }

    private Date cloneDate(Date date) {
        return date == null ? null : new Date(date.getTime());
    }
}
