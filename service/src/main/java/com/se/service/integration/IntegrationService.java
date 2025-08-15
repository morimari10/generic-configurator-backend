package com.se.service.integration;

import com.se.domain.holder.MainConfigHolder;

public interface IntegrationService {

    /**
     * Get selection guide configuration.
     *
     * @param selectionGuide selectionGuide
     * @param locale         locale
     * @return configuration as text
     */
    String getConfigurationStr(String selectionGuide, String locale);

    /**
     * @param locale locale
     * @param overrideMainSelectionGuide main selection guide
     * @return configuration as holder
     */
    MainConfigHolder getMainConfiguration(String locale, String overrideMainSelectionGuide);
    
}
