package com.se.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.se.common.GlobalConstants;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides utility methods working with headers.
 */
public final class HeadersUtils {

    private HeadersUtils() {
        throw new AssertionError("Constructor should not be called directly");
    }

    /**
     * Extract session id from headers.
     *
     * @param headers the headers
     * @return session id
     */
    public static String extractSessionId(Map<String, String> headers) {
        Map<String, String> headerLowerCase = Optional.ofNullable(headers)
                .map(Map::entrySet)
                .map(Collection::stream)
                .map(stream -> stream.collect(Collectors.toMap(entry -> entry.getKey().toLowerCase(),
                        Map.Entry::getValue)))
                .orElse(Maps.newHashMap());
        return Optional.of(headerLowerCase)
                .map(entry -> entry.get(GlobalConstants.COOKIE))
                .map(cookie -> Splitter.on(GlobalConstants.SPLITTER).omitEmptyStrings().trimResults()
                        .splitToList(cookie))
                .map(cookieList -> cookieList.stream().map(value -> value.split(GlobalConstants.KEY_VALUE_SEPARATOR))
                        .collect(Collectors.toMap(value -> value[0], value -> {
                            try {
                                return value[1];
                            } catch (ArrayIndexOutOfBoundsException a) {
                                return value[0];
                            }
                        }, (value1, value2) -> value1)))
                .map(map -> map.get(GlobalConstants.SESSION_ID))
                .orElse(null);
    }
    
}
