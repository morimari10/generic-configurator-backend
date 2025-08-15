package com.se.service.module;

import com.se.utils.BasePropertyModule;
import com.typesafe.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract properties module.
 */
public abstract class PropertiesModule extends BasePropertyModule {

    protected List<String> getProperties(Config config, String key) {
        String value = getProperty(config, key);
        return Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
