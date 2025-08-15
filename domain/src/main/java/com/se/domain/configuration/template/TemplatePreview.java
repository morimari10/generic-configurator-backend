package com.se.domain.configuration.template;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain object that represents template preview entity in database.
 */
@DynamoDBDocument
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplatePreview {
    private String blank;
    private String landscape;
    private String portrait;
}
