package com.se.storage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.se.domain.configuration.ErrorEventEntity;
import com.se.utils.database.ApplicationDynamoDBMapper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.inject.Inject;

/**
 * The implementation of DAO layer contract for interacting with storage.
 */
public class ErrorEventDAO extends AbstractCommonDAO<ErrorEventEntity> {

    /**
     * Constructor.
     *
     * @param dynamoDBMapper dynamo db mapper.
     */
@SuppressFBWarnings("BC_UNCONFIRMED_CAST")
    @Inject
    public ErrorEventDAO(DynamoDBMapper dynamoDBMapper) {
        super((ApplicationDynamoDBMapper) dynamoDBMapper, ErrorEventEntity.class);
    }

}
