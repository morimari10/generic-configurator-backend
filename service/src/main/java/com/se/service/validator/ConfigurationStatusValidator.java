package com.se.service.validator;

import com.google.common.collect.Lists;
import com.se.domain.configuration.ConfigurationStatus;

import java.util.List;

/**
 * Validator for the {@link ConfigurationStatus}.
 */
public class ConfigurationStatusValidator extends Validator<ConfigurationStatus> {

    private static final List<ConfigurationStatus> SUPPORTED_CONFIG_STATUSES
            = Lists.newArrayList(ConfigurationStatus.IN_PROGRESS, ConfigurationStatus.FINISHED,
            ConfigurationStatus.REGENERATED, ConfigurationStatus.OBSOLETE);

    @Override
    public boolean validate(ConfigurationStatus configurationStatus) {
        return SUPPORTED_CONFIG_STATUSES.contains(configurationStatus);
    }
}
