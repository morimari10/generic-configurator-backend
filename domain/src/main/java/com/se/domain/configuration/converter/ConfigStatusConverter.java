package com.se.domain.configuration.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.se.domain.configuration.ConfigurationStatus;

/**
 * Converter for configuration status.
 */
public class ConfigStatusConverter implements DynamoDBTypeConverter<String, ConfigurationStatus> {

    @Override
    public String convert(ConfigurationStatus object) {
        return object.name();
    }

    @Override
    public ConfigurationStatus unconvert(String object) {
        return ConfigurationStatus.valueOf(object);
    }
}
