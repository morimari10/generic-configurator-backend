package com.se.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import java.util.Optional;

/**
 * Base property module that include some common methods.
 */
public class BasePropertyModule {

    /**
     * Get property from config.
     *
     * @param config the config instance.
     * @param key    the property key.
     * @return founded property.
     */
    protected String getProperty(Config config, String key) {
        return Optional.ofNullable(key)
                .map(value -> {
                    try {
                        return config.getString(value);
                    } catch (ConfigException exception) {
                        return null;
                    }
                })
                .orElseThrow(() -> new RuntimeException(String.format("Property with key %s is not found", key)));
    }
}
