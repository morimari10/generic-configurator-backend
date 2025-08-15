package com.se.service.configuration;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.util.json.Jackson;
import com.google.common.collect.Lists;
import com.se.domain.configuration.ConfigurationStatus;
import com.se.domain.configuration.Configuration;
import com.se.domain.configuration.ConfigurationEntity;
import com.se.domain.configuration.ShortConfiguration;
import com.se.service.validator.Validator;
import com.se.storage.dao.ConfigurationDBDao;

public class ConfigurationServiceImpl implements ConfigurationService {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationServiceImpl.class);
    private static final int YEAR_EXPIRES = 5;

    private static final List<ConfigurationStatus> FIXED_STATUSES = Arrays.asList(
        ConfigurationStatus.FINISHED, ConfigurationStatus.OBSOLETE,
        ConfigurationStatus.CANCELLED
    );

    ConfigurationDBDao configurationDBDao;

    /**
     * Constructor.
     *
     */
    @Inject
    public ConfigurationServiceImpl(ConfigurationDBDao configurationDBDao) {
        this.configurationDBDao = configurationDBDao;
    }

    @Override
    public Configuration createConfiguration(String body, String sessionId,
        String seamlessId) {
        Configuration configuration = Jackson.fromJsonString(body,
        Configuration.class);
        LOGGER.debug("Executing creating configuration for configuration object - [{}]",
                configuration);
        Validator.validateSessionId(sessionId);
        ConfigurationEntity entity = new ConfigurationEntity();
        entity.setId(UUID.randomUUID().toString());

        configuration.setStatus(
            Optional.ofNullable(configuration.getStatus())
                .orElse(ConfigurationStatus.IN_PROGRESS)
        );

        entity.setCreatedDate(new Date());
        entity.setUpdatedDate(entity.getCreatedDate());
        entity.setConfigurationData(configuration);
        entity.setSessionId(sessionId);
        entity.setExpirationDate(Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                .plusYears(YEAR_EXPIRES)
                .toInstant()));
        return saveConfig(entity).getConfigurationData();
    }


    @Override
    public Configuration saveConfiguration(String id, String configurationJson, 
        String sessionId,
        String seamlessId) {

        Configuration configuration = Jackson.fromJsonString(configurationJson,
        Configuration.class);

        final ConfigurationEntity entity = configurationDBDao.getItem(id);
        final ConfigurationStatus currentStatus = entity.getConfigurationData().getStatus();

       if (!Optional.ofNullable(entity.getSeamlessId()).isPresent()
            && !Objects.equals(sessionId, entity.getSessionId())
            || Optional.ofNullable(entity.getSeamlessId()).isPresent() && !Objects
            .equals(entity.getSeamlessId(), seamlessId)
            || FIXED_STATUSES.contains(currentStatus)) {
            String uuid = UUID.randomUUID().toString();
            LOGGER.debug("Attempt to update a configuration not owned by current user or with fixed status");
            LOGGER.debug("Creating a copy of configuration with identifier - [{}]", uuid);
            entity.setId(uuid);
            entity.setSessionId(sessionId);
            entity.getConfigurationData().setCountry(configuration.getCountry());
            entity.setCreatedDate(new Date());
            entity.getConfigurationData().setStatus(ConfigurationStatus.FINALIZED);
        } else {
            // Otherwise keep old host
            configuration.setHost(entity.getConfigurationData().getHost());
            // Otherwise keep old campaignId
            configuration.setCampaignId(entity.getConfigurationData().getCampaignId());
            // Otherwise keep old urlParams
            configuration.setUrlParams(entity.getConfigurationData().getUrlParams());
        }
        entity.setSeamlessId(seamlessId);

        // Remove duplicate seen steps
        entity.getConfigurationData().setSeenSteps(
            configuration.getSeenSteps().stream().distinct()
            .collect(Collectors.toList())
        );

        entity.setConfigurationData(configuration);

        return this.saveConfig(entity).getConfigurationData();
    }

    @Override
    public Configuration getConfigurationById(String id) {
        LOGGER.debug("Attempt to find configuration by the following identifier - [{}]", id);
        ConfigurationEntity entity = new ConfigurationEntity();
        try {
            entity = configurationDBDao.getItem(id);
        } catch (Exception e) {
            LOGGER.error("Error while trying to recover configuration by id: [{}]",
                    e.getMessage());
        }
        return entity.getConfigurationData();
    }

    @Override
    public List<ShortConfiguration> getConfigurations(String sessionId) {
        LOGGER.debug("Attempt to find configurations by the session");
        List<ShortConfiguration> result = new ArrayList<>();
        Optional.ofNullable(sessionId)
            .map(configurationDBDao::getConfigurations)
            .orElse(Lists.newArrayList())
            .forEach(value -> {
                result.add(
                    ShortConfiguration.from(value)
                );
            });
        return result;
    }

    @Override
    public List<ShortConfiguration> getSeamlessConfigurations(String sessionId, String seamlessId) {
        LOGGER.debug("Attempt to find seamless configurations by the session");
        List<ShortConfiguration> result = new ArrayList<>();

        configurationDBDao
            .getSeamlessConfigurations(sessionId, seamlessId).stream()
            .forEach(value -> {
                result.add(
                    ShortConfiguration.from(value)
                );
            });
        return result;
    }

    @Override
    public ShortConfiguration getActiveConfiguration(String sessionId, String country) {
        LOGGER.debug("Attempt to find the last unfinished configuration"
            + " by the following session identifier - [{}]", sessionId);
        return Optional.ofNullable(sessionId)
            .map(id -> configurationDBDao.getActiveConfiguration(sessionId, country))
            .orElse(null);
    }

    @Override
    public ShortConfiguration getSeamlessActiveConfiguration(String seamlessId, String country) {
        LOGGER.debug("Attempt to find the last unfinished seamless configuration"
            + " by the following session identifier - [{}]", seamlessId);
        return Optional.ofNullable(seamlessId)
            .map(id -> configurationDBDao.getSeamlessActiveConfiguration(seamlessId, country))
            .orElse(null);
    }

    private ConfigurationEntity saveConfig(ConfigurationEntity entity) {
        Instant start = Instant.now();
        final Date now = new Date();
        entity.setUpdatedDate(now);
        
        configurationDBDao.saveItem(entity);

        final String savedId = entity.getId();
        Instant finish = Instant.now();

        LOGGER
            .debug("Configuration [{}] was successfully saved, it took [{} ms]",
                savedId, Duration.between(start, finish).toMillis());

        return entity;
    }

}
