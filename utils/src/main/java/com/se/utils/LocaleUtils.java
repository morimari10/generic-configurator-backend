package com.se.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Utility class for extracting locale.
 */
public final class LocaleUtils {

    private static final String LOCALE_PATTERN = "^[A-Za-z]{2}-[A-Za-z]{2}$";
    public static final String DEFAULT_LOCALE = "en-WW";

    private LocaleUtils() {
        throw new AssertionError("Constructor should not be called directly");
    }

    /**
     * Extract locale from string.
     *
     * @param locale the locale.
     * @return extracted locale.
     */
    public static Locale getLocale(String locale) {
        if (StringUtils.isNotBlank(locale)) {
            return locale.matches(LOCALE_PATTERN) ? new Locale.Builder().setLanguageTag(locale).build()
                    : new Locale.Builder().setLanguageTag(DEFAULT_LOCALE).build();
        } else {
            return new Locale.Builder().setLanguageTag(DEFAULT_LOCALE).build();
        }
    }
}
