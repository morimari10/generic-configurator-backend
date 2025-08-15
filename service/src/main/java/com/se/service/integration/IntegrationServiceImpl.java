package com.se.service.integration;


import javax.inject.Inject;

import java.util.Optional;

import com.amazonaws.util.json.Jackson;
import com.se.domain.holder.MainConfigHolder;
import com.se.service.httpClient.HttpClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class IntegrationServiceImpl implements IntegrationService {

    private static final Logger LOGGER = LogManager.getLogger(IntegrationServiceImpl.class);

    private final HttpClient httpClient;
    private final String sncMainGuide;
    private final Boolean enableMainOverride;

    /**
     * Constructor.
     *
     */
    @Inject
    public IntegrationServiceImpl(HttpClient httpClient, String sncMainGuide, Boolean enableMainOverride) {
        this.httpClient = httpClient;
        this.sncMainGuide = sncMainGuide;
        this.enableMainOverride = enableMainOverride;
    }


    @Override
    public MainConfigHolder getMainConfiguration(String locale, String overrideMainSelectionGuide) {
        String mainSelectionGuide = this.sncMainGuide;
        LOGGER.debug("Start Main configuration retrieval");

        if (this.enableMainOverride && Optional.ofNullable(overrideMainSelectionGuide).isPresent()) {
            mainSelectionGuide = overrideMainSelectionGuide;
        }

        final String mainConfiguration = this.getMainPLCConfigurationStr(locale, mainSelectionGuide);
        MainConfigHolder result = Jackson.fromJsonString(mainConfiguration, MainConfigHolder.class);

        LOGGER.debug("Result [{}]", result);

        
        return result;
    }

    @Override
    public String getConfigurationStr(String selectionGuide, String locale) {
        return this.httpClient.getConfiguration(selectionGuide, locale);
    }

    private String getMainPLCConfigurationStr(String locale, String mainSelectionGuide) {
        return this.getConfigurationStr(mainSelectionGuide, locale);
    }

}
