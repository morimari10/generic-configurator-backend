package com.se.utils.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.se.domain.configuration.ShortConfiguration;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationsResponse {
    private ShortConfiguration activeConfiguration;
    private ShortConfiguration seamlessActiveConfiguration;
    private List<ShortConfiguration> configurations;
}
