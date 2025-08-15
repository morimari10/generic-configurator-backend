package com.se.domain.configuration;

import java.util.LinkedList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.domain.configuration.converter.ConfigStatusConverter;
import com.se.domain.configuration.converter.StepConverter;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Domain object that represents configuration entity in database.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@DynamoDBDocument
public class Configuration {

    private String id;

    // DynamoDBAttribute 'name' can't be created due to reserved AWS keywords. 'configurationName' used instead.
    // More information here: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ReservedWords.html
    @DynamoDBAttribute(attributeName = "configurationName")
    private String name;

    // DynamoDBAttribute 'status' can't be created due to reserved AWS keywords. 'configurationStatus' used instead.
    // More information here: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ReservedWords.html
    @DynamoDBTypeConverted(converter = ConfigStatusConverter.class)
    @DynamoDBAttribute(attributeName = "configurationStatus")
    private ConfigurationStatus status;

    @DynamoDBTypeConverted(converter = StepConverter.class)
    @DynamoDBAttribute(attributeName = "step")
    private Step step;

    @DynamoDBAttribute(attributeName = "locale")
    private String locale;

    @DynamoDBAttribute(attributeName = "country")
    private String country;

    @DynamoDBAttribute(attributeName = "host")
    private String host;

    @DynamoDBAttribute(attributeName = "campaignId")
    private String campaignId;

    @DynamoDBAttribute(attributeName = "urlParams")
    private String urlParams;

    @DynamoDBAttribute(attributeName = "internal")
    private boolean internal;

    @DynamoDBAttribute(attributeName = "seenSteps")
    private List<String> seenSteps;

    /**
     * Constructor.
     */
    public Configuration() {
        this.seenSteps = new LinkedList<>();
    }
}
