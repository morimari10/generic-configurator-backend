package com.se.domain.configuration.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.se.domain.configuration.event.EventType;

/**
 * Converter for event type.
 */
public class EventTypeConverter implements DynamoDBTypeConverter<String, EventType> {

    @Override
    public String convert(EventType object) {
        return object.name();
    }

    @Override
    public EventType unconvert(String object) {
        return EventType.valueOf(object);
    }
}
