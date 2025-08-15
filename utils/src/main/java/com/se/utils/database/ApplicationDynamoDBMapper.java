package com.se.utils.database;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.amazonaws.util.VersionInfoUtils;
import com.se.domain.DBEntity;

import java.util.Collections;
import java.util.Map;

/**
 * Utility class for domain-object interaction with DynamoDB.
 */
public class ApplicationDynamoDBMapper extends DynamoDBMapper {

    private final DynamoDBMapperConfig config;
    private final AmazonDynamoDB db;

    //private static final Logger LOGGER = LogManager.getLogger();

    /**
     * User agent for requests made using the {@link DynamoDBMapper}.
     */
    private static final String USER_AGENT = DynamoDBMapper.class.getName() + "/" + VersionInfoUtils.getVersion();

    /**
     * Constructs a new mapper with the service object and configuration given.
     *
     * @param dynamoDB  The service object to use for all service calls.
     * @param config    The default configuration to use for all service calls. It can
     *                  be overridden on a per-operation basis.
     */
    public ApplicationDynamoDBMapper(final AmazonDynamoDB dynamoDB, final DynamoDBMapperConfig config) {
        super(dynamoDB, config, null, null);
        this.config = mergeConfig(config);
        this.db = dynamoDB;
    }

    /**
     * Do an update item and return result.
     *
     * @param entityClass       the item entity class.
     * @param updateItemRequest the update item request.
     * @return                  the update result on success, else 'null'.
     */
    public <T extends DBEntity> T genericUpdateItem(Class<T> entityClass, UpdateItemRequest updateItemRequest) {
        return genericUpdateItem(entityClass, updateItemRequest, this.config);
    }

    /**
     * Do an update item and return result.
     *
     * @param entityClass       the item entity class.
     * @param updateItemRequest the update item request.
     * @param config            the config override.
     * @return                  the update result on success, else 'null'.
     */
    public <T extends DBEntity> T genericUpdateItem(Class<T> entityClass, UpdateItemRequest updateItemRequest,
        DynamoDBMapperConfig config) {
        T resultEntity = null;
        if (null != updateItemRequest) {
            config = mergeConfig(config);
            String tableName = getTableName(entityClass, config);
            updateItemRequest.setTableName(tableName);
            UpdateItemResult updateResult = this.db.updateItem(applyUserAgent(updateItemRequest));
            if (updateResult.getAttributes() != null
                && !updateResult.getAttributes().isEmpty()) {
                Map<String, AttributeValue> itemAttributes = updateResult.getAttributes();
                if (null != itemAttributes) {
                    resultEntity = buildObjectFromRequestResult(itemAttributes, entityClass, config);
                }
            }

        }
        return resultEntity;
    }

    /**
     * Create a primary key.
     *
     * @param entityClass       the item entity class.
     * @param hashKey           the item hash key.
     * @param rangeKey          the item range key if some, else 'null'.
     * @return                  the primary key.
     */
    public <T extends DBEntity> T createPrimaryKey(Class<T> entityClass, Object hashKey, Object rangeKey) {
        return createPrimaryKey(entityClass, hashKey, rangeKey, this.config);
    }

    /**
     * Create a primary key.
     *
     * @param entityClass       the item entity class.
     * @param hashKey           the item hash key.
     * @param rangeKey          the item range key if some, else 'null'.
     * @param config            the config override.
     * @return                  the primary key.
     */
    public <T extends DBEntity> T createPrimaryKey(Class<T> entityClass, Object hashKey, Object rangeKey,
        DynamoDBMapperConfig config) {
        config = mergeConfig(config);
        final DynamoDBMapperTableModel<T> model = getTableModel(entityClass, config);
        return model.createKey(hashKey, rangeKey);
    }

    /**
     * Create a primary key map with a single primary key.
     *
     * @param entityClass       the item entity class.
     * @param hashKey           the item hash key.
     * @param rangeKey          the item range key if some, else 'null'.
     * @return                  the primary key map.
     */
    public <T extends DBEntity> Map<String, AttributeValue> createPrimaryKeyMap(Class<T> entityClass,
        Object hashKey, Object rangeKey) {
        return createPrimaryKeyMap(entityClass, hashKey, rangeKey, this.config);
    }

    /**
     * Create a primary key map with a single primary key.
     *
     * @param entityClass       the item entity class.
     * @param hashKey           the item hash key.
     * @param rangeKey          the item range key if some, else 'null'.
     * @param config            the config override.
     * @return                  the update result on success, else 'null'.
     */
    public <T extends DBEntity> Map<String, AttributeValue> createPrimaryKeyMap(Class<T> entityClass, Object hashKey,
        Object rangeKey, DynamoDBMapperConfig config) {
        T primaryKey = createPrimaryKey(entityClass, hashKey, rangeKey, config);
        final DynamoDBMapperTableModel<T> model = getTableModel(entityClass, config);
        return model.convertKey(primaryKey);
    }

    /**
     * Return an object from a request result.
     *
     * @param parameters    the item entity class.
     * @return the request result as an object.
     */
    private <T> T buildObjectFromRequestResult(final Map<String, AttributeValue> attributeValues,
        final Class<T> modelClass, final DynamoDBMapperConfig mapperConfig) {
        final Map<String, AttributeValue> values = Collections.unmodifiableMap(attributeValues);
        final DynamoDBMapperTableModel<T> model = getTableModel(modelClass, mapperConfig);
        return model.unconvert(values);
    }

    /**
     * Apply user agent to a given request.
     *
     * @param request   the request to apply the user agent to.
     * @return          the updated request.
     */
    private static <RequestTypeT extends AmazonWebServiceRequest> RequestTypeT applyUserAgent(RequestTypeT request) {
        request.getRequestClientOptions().appendUserAgent(USER_AGENT);
        return request;
    }
 
}
