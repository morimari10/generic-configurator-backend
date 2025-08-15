package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.util.json.Jackson;
import com.se.domain.configuration.ErrorEventEntity;
import com.se.storage.dao.ErrorEventDAO;
import com.se.utils.database.ApplicationDynamoDBMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

/**
 * The implementation of {@link Action} for saving event data.
 */
public class LogErrorEventAction extends AbstractAction<String> {

    private static final Logger LOGGER = LogManager.getLogger(LogErrorEventAction.class);
    private final ApplicationDynamoDBMapper dynamoDBMapper;

    /**
     * Parametrized constructor.
     *
     * @param dynamoDBMapper service for saving event data.
     */
    @Inject
    public LogErrorEventAction(ApplicationDynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String perform(AwsProxyRequest request, String errorCode) {
        LOGGER.debug("Storing the following event in the error event table - [{}]",
                request);
        ErrorEventDAO errorEventDAO = new ErrorEventDAO(dynamoDBMapper);
        ErrorEventEntity errorEventEntity = new ErrorEventEntity();
        errorEventEntity.setId(UUID.randomUUID().toString());
        errorEventEntity.setAction(request.getPath());
        errorEventEntity.setHeaders(request.getHeaders());
        errorEventEntity.setPayload(Jackson.fromJsonString(request.getBody(),
                Map.class));
        errorEventEntity.setParameters(request.getQueryStringParameters());
        errorEventEntity.setPathVariables(request.getPathParameters());
        errorEventEntity.setErrorCode(errorCode);
        errorEventEntity.setCreatedDate(new Date());
        errorEventDAO.saveItem(errorEventEntity);
        return errorEventEntity.getId();
    }

    @Override
    public String perform(String body, Map<String, String> query) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
