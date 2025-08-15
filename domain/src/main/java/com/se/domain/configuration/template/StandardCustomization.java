package com.se.domain.configuration.template;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain object that represents standard customizations.
 */
@DynamoDBDocument
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardCustomization {
    @DynamoDBAttribute
    private String jsonLink;
    @DynamoDBAttribute
    private String imageLink;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DynamoDBAttribute
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DynamoDBAttribute
    private String description;
}
