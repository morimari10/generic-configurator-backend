package com.se.storage.dao;

import com.se.domain.configuration.event.EventData;
import com.se.utils.database.ApplicationDynamoDBMapper;

/**
 * Dao for interaction with {@link EventData} in DynamoDB.
 */
public class EventDao extends AbstractCommonDAO<EventData> {

    /**
     * Parametrized constructor.
     *
     * @param dynamoDBMapper dynamo db mapper.
     */
    public EventDao(ApplicationDynamoDBMapper dynamoDBMapper) {
        super(dynamoDBMapper, EventData.class);
    }
}
