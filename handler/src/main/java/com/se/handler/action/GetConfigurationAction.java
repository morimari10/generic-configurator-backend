package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.se.domain.configuration.Configuration;
import com.se.service.configuration.ConfigurationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * The implementation of {@link Action} for retrieving a configuration from it's ID.
 */
public class GetConfigurationAction extends AbstractAction<Configuration> {

    private static final Logger LOGGER = LogManager.getLogger(GetConfigurationAction.class);
    private static final String ID_PATH_VARIABLE = "id";

    private ConfigurationService configurationService;

    /**
     * Parametrized constructor.
     *
     * @param configurationService session service.
     */
    public GetConfigurationAction(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public Configuration perform(AwsProxyRequest request) {
        LOGGER.info("Attenpt to retrieve a configuration by its ID");
        final String configId = request.getPathParameters().get(ID_PATH_VARIABLE);
        return configurationService.getConfigurationById(
            configId
        );
    }

    @Override
    public Configuration perform(String body, Map<String, String> query) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }

    @Override
    public Configuration perform(AwsProxyRequest request, String s) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
