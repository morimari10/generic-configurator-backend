package com.se.domain.configuration.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.se.domain.configuration.Step;

/**
 * Converter for configuration step.
 */
public class StepConverter implements DynamoDBTypeConverter<String, Step> {

    @Override
    public String convert(Step object) {
        return object.name();
    }

    @Override
    public Step unconvert(String object) {
        return Step.valueOf(object);
    }
}
