package com.se.utils;

import java.util.Map;
import java.util.Optional;

/**
 * Util class to retrieve request information.
 */
public final class RequestUtils {

    private RequestUtils() {
        throw new AssertionError("Constructor should not be called directly");
    }

    /**
     * Retrieve the locale value from the request map.
     *
     * @param requestMap request parameters map
     * @return the locale variable value
     */
    public static String getLocaleVariableValue(Map<String, String> requestMap) {
        return RequestUtils.getVariableValue(requestMap, "locale", LocaleUtils.DEFAULT_LOCALE);
    }

    /**
     * Retrieve the seamless id from the request map.
     *
     * @param requestMap request parameters map
     * @return the seamless id value
     */
    public static String getSeamlessIdValue(Map<String, String> requestMap) {
        return RequestUtils.getVariableValue(requestMap, "seamlessId", null);
    }

    /**
     * Retrieve a variable value from the request map.
     *
     * @param requestMap request parameters map
     * @param variableName the variable name
     * @param defaultValue the variable default value if not found
     * @return the variable value
     */
    public static String getVariableValue(Map<String, String> requestMap, String variableName, String defaultValue) {
        return Optional.ofNullable(
            requestMap.get(variableName)
        ).filter(s -> !s.isEmpty()).orElse(defaultValue);
    }
}
