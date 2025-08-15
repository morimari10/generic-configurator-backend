package com.se.service.configuration;

import java.util.List;

import com.se.domain.configuration.Configuration;
import com.se.domain.configuration.ShortConfiguration;

public interface ConfigurationService {

    /**
     * Create configuration.
     *
     * @param body      the body.
     * @param sessionId the session id.
     * @param seamlessId the seamless id
     * @return configuration object
     */
    Configuration createConfiguration(String body, String sessionId,
            String seamlessId);

    /**
     * Save configuration.
     *
     * @param body      the body.
     * @param sessionId the session id.
     * @param seamlessId the seamless id
     * @return configuration object
     */
    Configuration saveConfiguration(String id, String configurationJson, 
        String sessionId,
        String seamlessId);

    /**
     * Get configuration.
     * 
     * @param id id
     * @return configuration
     */
    Configuration getConfigurationById(String id);

    /**
     * Looks for configurations by the user session identifier.
     *
     * @param sessionId the session identifier
     * @return the found list of short configuration
     */
    List<ShortConfiguration> getConfigurations(String sessionId);

    /**
     * Looks for seamless configurations by the user session identifier.
     *
     * @param sessionId the session identifier
     * @param seamlessId the seamless identifier
     * @return the found list of short configuration
     */
    List<ShortConfiguration> getSeamlessConfigurations(String sessionId, String seamlessId);


    /**
     * Retrieve the last active configuration from public website, for the specified country.
     * 
     * @param sessionId session identifier
     * @param country   country
     * @return the last active configuration if it exists, null otherwise
     */
    ShortConfiguration getActiveConfiguration(String sessionId, String country);


    /**
     * Retrieve the last active configuration from MySchneider, for the specified country.
     * 
     * @param seamlessId seamless identifier
     * @param country   country
     * @return
     */
    ShortConfiguration getSeamlessActiveConfiguration(String seamlessId, String country);
}
