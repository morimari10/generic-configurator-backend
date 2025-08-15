package com.se.storage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.se.common.ErrorCode;
import com.se.domain.DBEntity;
import com.se.domain.exception.DatabaseException;
import com.se.domain.exception.RecordNotFoundException;
import com.se.utils.database.ApplicationDynamoDBMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The implementation of DAO layer contract for interacting with storage.
 *
 * @param <T> Entity type.
 */
public abstract class AbstractCommonDAO<T extends DBEntity> implements CommonDAO<T> {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Dynamo db mapper.
     */
    protected final ApplicationDynamoDBMapper dynamoDBMapper;

    /**
     * Entity type to use.
     */
    private final Class<T> entityType;

    /**
     * Constructor.
     *
     * @param dynamoDBMapper dynamo db mapper.
     * @param entityType     entity type.
     */
    public AbstractCommonDAO(ApplicationDynamoDBMapper dynamoDBMapper, Class<T> entityType) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.entityType = entityType;
    }

    @Override
    public void saveItem(T entity) {
        try {
            LOGGER.debug("Attempt to save entity - [{}]", entity);
            dynamoDBMapper.save(entity);
        } catch (AmazonDynamoDBException exception) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while saving entity [{}]. Error: [{}]: [{}]",
                    entity, errorCode, exception.getMessage());
            throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }

    @Override
    public T getItem(String id) {
        try {
            LOGGER.debug("Attempt to get entity by id - [{}]", id);
            return Optional.ofNullable(dynamoDBMapper.load(entityType, id))
                    .orElseThrow(() -> new RecordNotFoundException(ErrorCode.DATA_NOT_FOUND));
        } catch (AmazonDynamoDBException exception) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while getting entity by id [{}]. Error: [{}]: [{}]",
                    id, errorCode, exception.getMessage());
            throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }

    /**
     * Gets entity with atomic counter from storage by id, with atomic counter update.
     * Counter is reset on {@link counterMaxValue} overflow.
     *
     * @param id                id.
     * @param counterPath       atomic counter path as a java class property path (ie : "innerData[2].counter").
     * @param counterMaxValue   atomic counter max value.
     * @param initialEntity     the initial atomic counter entity if it has to be created, else 'null'.
     * @return entity.
     */
    @Override
    public T getItemWithAtomicCounterAndUpdate(String id, String counterPath, long counterMaxValue, T initialEntity) {
        return getItemWithAtomicCounterAndUpdate(this.entityType, id, counterPath, counterMaxValue, initialEntity);
    }

    /**
     * Gets entity with atomic counter from storage by id, with atomic counter update.
     * Counter is reset on {@link counterMaxValue} overflow.
     *
     * @param entityType        The item type.
     * @param id                id.
     * @param counterPath       atomic counter path as a java class property path (ie : "innerData[2].counter").
     * @param counterMaxValue   atomic counter max value.
     * @param initialEntity     the initial atomic counter entity if it has to be created, else 'null'.
     * @return entity.
     */
    @Override
    public <ItemTypeT extends T> ItemTypeT getItemWithAtomicCounterAndUpdate(Class<ItemTypeT> entityType,
        String id, String counterPath, long counterMaxValue, ItemTypeT initialEntity) {
        LOGGER.debug("getItemWithAtomicCounterAndUpdate with id [{}] and path [{}]", id, counterPath);

        final Map<String, AttributeValue> primaryKeyMap
            = this.dynamoDBMapper.createPrimaryKeyMap(entityType, id, null);
        final String updateExpression = "SET " + counterPath + "=" + counterPath + "+:incr";
        final String conditionExpression = counterPath + "<=" + ":max";
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":incr", new AttributeValue().withN("1"));
        expressionAttributeValues.put(":max", new AttributeValue().withN(Long.toString(counterMaxValue)));

        boolean redoRequest = false;
        boolean redoDone = false;

        do {
            redoDone = redoRequest;

            try {
                LOGGER.debug("Attempt to get atomic counter by id [{}] and path [{}]", id, counterPath);
                // build request for atomic counter
                UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                    .withKey(primaryKeyMap)
                    .withUpdateExpression(updateExpression)
                    .withConditionExpression(conditionExpression)
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withReturnValues(ReturnValue.ALL_OLD);
                // do request
                ItemTypeT item = this.dynamoDBMapper.genericUpdateItem(entityType, updateItemRequest);
                //LOGGER.debug("AtomicCounter return item {}", item);
                return Optional.ofNullable(item)
                    .orElseThrow(() -> new RecordNotFoundException(ErrorCode.DATA_NOT_FOUND));
            } catch (ConditionalCheckFailedException exception) {
                // counter has reached max value, should reset counter
                // only one try allowed
                if (false == redoRequest) {
                    redoRequest = true;
                    boolean tryResetCounter = false;
                    // first, try to create record if it does not exists
                    if (null != initialEntity) {
                        try {
                            LOGGER.debug("AtomicCounter create counter [{}]", initialEntity);
                            DynamoDBSaveExpression saveExpression
                                = new DynamoDBSaveExpression().withExpectedEntry(counterPath,
                                    new ExpectedAttributeValue(false));
                            dynamoDBMapper.save(initialEntity, saveExpression);
                        } catch (ConditionalCheckFailedException innerException) {
                            // if counter has been created in another thread (from another client), nothing to do
                            LOGGER.debug("AtomicCounter create already done");
                            tryResetCounter = true;
                        }
                    } else {
                        tryResetCounter = true;
                    }

                    if (true == tryResetCounter) {
                        // second, try to reset counter
                        try {
                            LOGGER.debug("AtomicCounter reset counter");
                            // build request for counter restart
                            // add condition to do it only once in multi-client environment
                            Map<String, ExpectedAttributeValue> resetExpected
                                = new HashMap<String, ExpectedAttributeValue>();
                            resetExpected.put(counterPath,
                                new ExpectedAttributeValue(new AttributeValue().withN(Long.toString(counterMaxValue)))
                                    .withComparisonOperator(ComparisonOperator.GT));
                            // reset to 0
                            Map<String, AttributeValueUpdate> resetUpdates
                                = new HashMap<String, AttributeValueUpdate>();
                            resetUpdates.put(counterPath,
                                new AttributeValueUpdate(new AttributeValue().withN("0"), AttributeAction.PUT));
                            UpdateItemRequest restartCounterRequest = new UpdateItemRequest()
                                .withKey(primaryKeyMap)
                                .withAttributeUpdates(resetUpdates)
                                .withExpected(resetExpected);
                            // reset counter
                            this.dynamoDBMapper.genericUpdateItem(entityType, restartCounterRequest);
                        } catch (ConditionalCheckFailedException innerException) {
                            // if counter has been reset in another thread (from another client), nothing to do
                            LOGGER.debug("AtomicCounter reset already done");
                        }
                    }
                } else {
                    ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
                    LOGGER.error("Error occurred while getting atomic counter by id [{}]. Error: [{}]: [{}]",
                        id, errorCode, exception.getMessage());
                    throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                        errorCode.getDescription());
                }
            } catch (AmazonDynamoDBException exception) {
                ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
                LOGGER.error("Error occurred while getting atomic counter by id [{}]. Error: [{}]: [{}]",
                    id, errorCode, exception.getMessage());
                throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
            }
        } while (true == redoRequest && false == redoDone);

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getItems(List<T> itemsToGet) {
        try {
            LOGGER.debug("Attempt to get entities - [{}]", itemsToGet);
            String tableName = entityType.getAnnotation(DynamoDBTable.class).tableName();
            List<T> resultWithFilter = dynamoDBMapper.batchLoad(itemsToGet).entrySet().stream()
                    .filter(stringListEntry -> stringListEntry.getKey().contains(tableName))
                    .findFirst()
                    .orElseThrow(() -> new RecordNotFoundException(ErrorCode.DATA_NOT_FOUND))
                    .getValue().stream()
                    .map(o -> (T) o)
                    .collect(Collectors.toList());

            LOGGER.info("AbstractCommon resultfilter - [{}]", resultWithFilter);

            if (resultWithFilter.isEmpty()) {

                ErrorCode errorCode = ErrorCode.DATA_NOT_FOUND;
                LOGGER.debug(" getHttpCode -[{}]  ", errorCode.getHttpCode());
                LOGGER.debug(" getKey  - [{}]",errorCode.getKey());
                LOGGER.debug(" getDescription - [{}] ",errorCode.getDescription());

                throw new RecordNotFoundException(errorCode.getHttpCode(),errorCode.getKey(),
                        errorCode.getDescription());
            } else {
                LOGGER.debug(" Attempt to get entities - [{}]", resultWithFilter);
            }

            return resultWithFilter;
        } catch (AmazonDynamoDBException exception) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while getting entities. Error: [{}]: [{}]",
                    errorCode, exception.getMessage());
            throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }

    @Override
    public List<T> scanItems(DynamoDBScanExpression scanExpression) {
        try {
            LOGGER.debug("Attempt to scan entities");
            return dynamoDBMapper.scan(entityType, scanExpression);
        } catch (AmazonDynamoDBException exception) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while scanning for entities. Error: [{}]: [{}]",
                    errorCode, exception.getMessage());
            throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }
}
