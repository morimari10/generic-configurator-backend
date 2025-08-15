package com.se.storage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.google.common.collect.ImmutableMap;
import com.se.domain.configuration.template.ProductType;
import com.se.domain.configuration.template.TemplateEntity;
import com.se.domain.exception.DatabaseException;
import com.se.common.ErrorCode;
import com.se.utils.database.ApplicationDynamoDBMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dao for interaction with templates in the DynamoDB.
 */
public class TemplateDao extends AbstractCommonDAO<TemplateEntity> {

    private static final Logger LOGGER = LogManager.getLogger(TemplateDao.class);

    /**
     * Constructor.
     *
     * @param dynamoDBMapper dynamo db mapper.
     */
    public TemplateDao(ApplicationDynamoDBMapper dynamoDBMapper) {
        super(dynamoDBMapper, TemplateEntity.class);
    }

    /**
     * Returns list of templates by type.
     *
     * @param productType template type
     * @return list with templates
     */
    public List<TemplateEntity> getTemplatesByType(ProductType productType) {
        LOGGER.debug("Attempt to find templates by type - [{}]", productType);
        DynamoDBQueryExpression<TemplateEntity> query = new DynamoDBQueryExpression<TemplateEntity>()
                .withIndexName(TemplateEntity.TEMPLATE_TYPE_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("templateType = :templateType")
                .withProjectionExpression("cr, size, disabledTemplate, gcr")
                .withSelect(Select.SPECIFIC_ATTRIBUTES)
                .withExpressionAttributeValues(ImmutableMap.<String, AttributeValue>builder()
                        .put(":templateType", new AttributeValue().withS(productType.getTemplateType()))
                        .build());
        try {
            List<TemplateEntity> templateEntities = dynamoDBMapper.query(TemplateEntity.class, query);
            LOGGER.info("Founded templates: - [{}]", templateEntities.stream().map(TemplateEntity::getCr)
                    .collect(Collectors.toList()));
            return templateEntities;
        } catch (AmazonDynamoDBException exception) {
            ErrorCode errorCode = ErrorCode.DATA_ACCESS_FAIL;
            LOGGER.error("Error occurred while getting templates by their type");
            throw new DatabaseException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }

    /**
     * Read template by specified template cr.
     *
     * @param cr the template commercial reference
     * @return founded template.
     */
    public TemplateEntity getTemplateById(String cr) {
        LOGGER.info("Attempt to find template by cr - [{}]", cr);
        return dynamoDBMapper.load(TemplateEntity.class, cr);
    }
}
