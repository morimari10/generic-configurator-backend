package com.se.service.configurationStep;

import com.se.service.httpClient.HttpClient;
import com.se.service.httpClient.HttpClientConverter;
import com.se.utils.model.configurationStep.ConfigurationResponse;


public interface ConfigurationStepService {
    /**
     * Get configuration data.
     *
     * @param body the body.
     * @return configuration response object
     */
    ConfigurationResponse configurationData(String body, HttpClient httpClient, HttpClientConverter httpClientConverter);
}
