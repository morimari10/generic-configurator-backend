package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.se.service.configuration.ConfigurationService;
import com.se.utils.HeadersUtils;
import com.se.utils.RequestUtils;
import com.se.utils.model.ConfigurationsResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

/**
 * The implementation of {@link Action} for retrieving configurations.
 */
public class GetConfigurationsAction extends AbstractAction<ConfigurationsResponse> {

    private static final Logger LOGGER = LogManager.getLogger(GetConfigurationAction.class);

    private ConfigurationService configurationService;

    /**
     * Parametrized constructor.
     *
     * @param configurationService session service.
     */
    public GetConfigurationsAction(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public ConfigurationsResponse perform(AwsProxyRequest request) {
        LOGGER.info("Attenpt to retrieve a configuration by its ID");
        String locale = RequestUtils.getLocaleVariableValue(
            request.getQueryStringParameters()
        );
        String sessionId = HeadersUtils.extractSessionId(request.getHeaders());
        String seamlessId = RequestUtils.getSeamlessIdValue(
            request.getQueryStringParameters()
        );

        final ConfigurationsResponse response = new ConfigurationsResponse();

        if (Optional.ofNullable(seamlessId).isPresent()) {
            response.setConfigurations(
                configurationService.getSeamlessConfigurations(sessionId, seamlessId)
            );
        } else {
            response.setConfigurations(
                configurationService.getConfigurations(sessionId)
            );
        }

        Optional.ofNullable(
            configurationService.getActiveConfiguration(sessionId, locale)
        ).ifPresent(activeConfiguration -> {
            response.setActiveConfiguration(activeConfiguration);
        });

        Optional.ofNullable(
            configurationService.getSeamlessActiveConfiguration(seamlessId, locale)
        ).ifPresent(activeConfiguration -> {
            response.setActiveConfiguration(activeConfiguration);
        });

        return response;
    }

    @Override
    public ConfigurationsResponse perform(String body, Map<String, String> query) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }

    @Override
    public ConfigurationsResponse perform(AwsProxyRequest request, String s) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
