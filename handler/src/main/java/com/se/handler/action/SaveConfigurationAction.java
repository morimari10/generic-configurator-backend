package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.se.domain.configuration.Configuration;
import com.se.service.configuration.ConfigurationService;
import com.se.utils.HeadersUtils;
import com.se.utils.RequestUtils;

import java.util.Map;

/**
 * The implementation of {@link Action} for saving a configuration.
 */
public class SaveConfigurationAction extends AbstractAction<Configuration> {

    private static final String ID_PATH_VARIABLE = "id";

    private ConfigurationService configurationService;

    /**
     * Parametrized constructor.
     *
     * @param configurationService the configuration service instance.
     */
    public SaveConfigurationAction(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public Configuration perform(AwsProxyRequest request) {

        final String configId = request.getPathParameters().get(ID_PATH_VARIABLE);

        return configurationService.saveConfiguration(configId,
            request.getBody(), 
            HeadersUtils.extractSessionId(request.getHeaders()),
            RequestUtils.getSeamlessIdValue(request.getQueryStringParameters()));
    }

    @Override
    public Configuration perform(String body, Map<String, String> query) {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " doesn't support this!");
    }

    @Override
    public Configuration perform(AwsProxyRequest request, String s) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
