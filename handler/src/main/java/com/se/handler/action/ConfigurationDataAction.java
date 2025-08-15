package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.se.service.configurationStep.ConfigurationStepService;
import com.se.service.httpClient.HttpClient;
import com.se.service.httpClient.HttpClientConverter;
import com.se.utils.model.configurationStep.ConfigurationResponse;

import java.util.Map;

/**
 * The implementation of {@link Action} for reading templates by list of
 * commercial references.
 */
public class ConfigurationDataAction extends AbstractAction<ConfigurationResponse> {

    private ConfigurationStepService configurationStepService;
    private HttpClient httpClient;
    private HttpClientConverter httpClientConverter;

    /**
     * Parametrized constructor.
     *
     * @param configurationService the specify service instance.
     */
    public ConfigurationDataAction(ConfigurationStepService configurationStepService, HttpClient httpClient,
            HttpClientConverter httpClientConverter) {
        this.configurationStepService = configurationStepService;
        this.httpClient = httpClient;
        this.httpClientConverter = httpClientConverter;
    }

    @Override
    public ConfigurationResponse perform(AwsProxyRequest request) {
        return configurationStepService.configurationData(request.getBody(), httpClient, httpClientConverter);
    }

    @Override
    public ConfigurationResponse perform(String body, Map<String, String> query) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }

    @Override
    public ConfigurationResponse perform(AwsProxyRequest request, String s) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
