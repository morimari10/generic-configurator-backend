package com.se.storage.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.se.common.ErrorCode;
import com.se.common.GlobalConstants;
import com.se.domain.configuration.ConfigurationStatus;
import com.se.domain.configuration.ConfigurationEntity;
import com.se.domain.configuration.ShortConfiguration;
import com.se.domain.exception.DatabaseException;
import com.se.utils.database.ApplicationDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.google.common.collect.ImmutableMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;


/**
 * Dao for interaction with configuration storage.
 */
public class ConfigurationDBDao extends AbstractCommonDAO<ConfigurationEntity> {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationDBDao.class);

    /**
     * Constructor.
     *
     * @param dynamoDBMapper dynamo db mapper.
     */
    public ConfigurationDBDao(ApplicationDynamoDBMapper dynamoDBMapper) {
        super(dynamoDBMapper, ConfigurationEntity.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<ConfigurationEntity> mGetConfigurations(String sessionId, Boolean withoutSeamlessConfigurations) {
        LOGGER.debug("Attempt to find finished configurations in storage for user");
        DynamoDBQueryExpression<ConfigurationEntity> query = new DynamoDBQueryExpression().withIndexName(GlobalConstants.SESSION_ID_INDEX).withConsistentRead(false).withKeyConditionExpression("sessionId = :sessionId").withFilterExpression("configurationStatus = :configurationStatus").withProjectionExpression("id, createdDate, updatedDate, sessionId, configurationStatus, configurationName, configuration").withSelect(Select.SPECIFIC_ATTRIBUTES).withExpressionAttributeValues(ImmutableMap.builder().put(":sessionId", new AttributeValue().withS(sessionId)).put(":configurationStatus", new AttributeValue().withS(ConfigurationStatus.FINISHED.name())).build());

        try {
            Stream<ConfigurationEntity> resultStream = this.dynamoDBMapper.query(ConfigurationEntity.class, query).stream();
            if (withoutSeamlessConfigurations) {
                resultStream = resultStream.filter((entity) -> {
                    return entity.getSeamlessId() == null;
                });
            }

            return (List)resultStream.collect(Collectors.toList());
        } catch (AmazonDynamoDBException var6) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while getting configurations by the session ");
            throw new DatabaseException(var6, errorCode.getHttpCode(), errorCode.getKey(), errorCode.getDescription());
        }
    }

    public List<ConfigurationEntity> getConfigurations(String sessionId) {
        return this.mGetConfigurations(sessionId, false);
    }

    /**
     * Gets list of configurations.
     *
     * @param sessionId the session identifier.
     * @param seamlessId the session identifier.
     * @return list of configurations.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<ConfigurationEntity> getSeamlessConfigurations(String sessionId, String seamlessId) {
        LOGGER.debug("Attempt to find finished configurations in storage for user");
        DynamoDBQueryExpression<ConfigurationEntity> query =
            new DynamoDBQueryExpression<ConfigurationEntity>()
                .withIndexName(ConfigurationEntity.SEAMLESS_ID_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("seamlessId = :seamlessId")
                .withFilterExpression("configurationData.configurationStatus = :configurationStatus")
                .withProjectionExpression("id, configurationData, createdDate, updatedDate")
                .withSelect(Select.SPECIFIC_ATTRIBUTES)
                .withExpressionAttributeValues(ImmutableMap.<String, AttributeValue>builder()
                    .put(":seamlessId", new AttributeValue().withS(seamlessId))
                    .put(":configurationStatus", new AttributeValue()
                        .withS(ConfigurationStatus.FINISHED.name()))
                    .build());
        try {
            List<ConfigurationEntity> result = new ArrayList<>();
            result.addAll(dynamoDBMapper
                .query(ConfigurationEntity.class, query));
            result.addAll(this.mGetConfigurations(sessionId, true));
            // remove eventual duplicates.
            return new ArrayList(new HashSet<>(result));
        } catch (AmazonDynamoDBException exception) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while getting seamless configurations for user");
            throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                errorCode.getDescription());
        }
    }

    /**
     * Gets active configuration by session identifier.
     *
     * @param sessionId the session identifier.
     * @param country the current country of the user
     * @return active configuration
     */
    public ShortConfiguration getActiveConfiguration(String sessionId, String country) {
        LOGGER.debug("Attempt to find active configuration in storage for user");
        DynamoDBQueryExpression<ConfigurationEntity> query =
            new DynamoDBQueryExpression<ConfigurationEntity>()
                .withIndexName(ConfigurationEntity.SESSION_ID_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("sessionId = :sessionId")
                .withProjectionExpression("id, configurationData, updatedDate, configurationStatus, seamlessId")
                .withSelect(Select.SPECIFIC_ATTRIBUTES)
                .withExpressionAttributeValues(ImmutableMap.<String, AttributeValue>builder()
                    .put(":sessionId", new AttributeValue().withS(sessionId))
                    .build());
        try {
            Optional<ConfigurationEntity> entityOpt = dynamoDBMapper.query(ConfigurationEntity.class, query)
                .stream()
                .filter(entity -> ConfigurationStatus.FINALIZED
                    .equals(entity.getConfigurationData().getStatus()))
                .filter(entity -> country.equals(entity.getConfigurationData().getCountry()))
                .filter(entity -> !Optional.ofNullable(entity.getSeamlessId()).isPresent())
                .min(Comparator.comparing(ConfigurationEntity::getUpdatedDate,
                    Comparator.nullsLast(Comparator.reverseOrder())));

            return entityOpt
                .map(ShortConfiguration::from)
                .orElse(null);

        } catch (AmazonDynamoDBException exception) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while getting configurations by the session");
            throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                errorCode.getDescription());
        }
    }

    /**
     * Gets active configuration by session identifier.
     *
     * @param seamlessId the seamless identifier.
     * @param country the current country of the user
     * @return active configuration
     */
    public ShortConfiguration getSeamlessActiveConfiguration(String seamlessId, String country) {
        LOGGER.debug("Attempt to find active configuration in storage for user");
        DynamoDBQueryExpression<ConfigurationEntity> query =
            new DynamoDBQueryExpression<ConfigurationEntity>()
                .withIndexName(ConfigurationEntity.SEAMLESS_ID_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("seamlessId = :seamlessId")
                .withProjectionExpression("id, configurationData, updatedDate, configurationStatus")
                .withSelect(Select.SPECIFIC_ATTRIBUTES)
                .withExpressionAttributeValues(ImmutableMap.<String, AttributeValue>builder()
                    .put(":seamlessId", new AttributeValue().withS(seamlessId))
                    .build());
        try {
            Optional<ConfigurationEntity> entityOpt = dynamoDBMapper.query(ConfigurationEntity.class, query)
                .stream()
                .filter(entity -> ConfigurationStatus.FINALIZED
                    .equals(entity.getConfigurationData().getStatus()))
                .filter(entity -> country.equals(entity.getConfigurationData().getCountry()))
                .min(Comparator.comparing(ConfigurationEntity::getUpdatedDate,
                    Comparator.nullsLast(Comparator.reverseOrder())));

            return entityOpt
                .map(ShortConfiguration::from)
                .orElse(null);

        } catch (AmazonDynamoDBException exception) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while getting configurations by the session");
            throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                errorCode.getDescription());
        }
    }
}
