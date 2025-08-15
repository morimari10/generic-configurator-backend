package com.se.common;

import java.util.Collection;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class IntegrationUtil {

    private static final Logger LOGGER = LogManager.getLogger(IntegrationUtil.class);

    /**
     * Extracts country from loc.
     *
     * @param loc locale tag.
     * @return country 2-letter code.
     */
    public static String extractCountry(String loc) {
        Locale locale = new Locale.Builder().setLanguageTag(loc).build();
        return locale.getCountry();
    }

        /**
     * Normalizes local so that S&C could use it for translations.
     * F.e. "fr-fr" -> "fr-FR"
     *
     * @param loc locale.
     * @return locale.
     */
    public static String normalizeLocale(String loc) {
        Locale locale = new Locale.Builder().setLanguageTag(loc).build();
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    /**
     * Extract country code from key for cfg tab in ez tables.
     *
     * @param key key with country code.
     * @return country code.
     */
    public static String extractCountryCodeFromConfig(String key) {
        int slashInd = StringUtils.indexOf(key, '/') + 1;
        String code = StringUtils.substring(key, slashInd);
        code = StringUtils.trim(code);
        code = StringUtils.upperCase(code);
        return code;
    }

    /**
     * Assert single item collection and extracts this element from collection.
     * If not - exception will be throwed.
     *
     * @param collection collection.
     * @param <T>        element type.
     * @return element.
     */
    public static <T> T getFirstItemCollection(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            LOGGER.warn("Data inconstancy error");
            throw new ExternalDataException("Data inconstancy", ErrorCode.EXTERNAL_DATA_INCONSTANCY.getHttpCode(),
                    ErrorCode.EXTERNAL_DATA_INCONSTANCY.getKey(),
                    ErrorCode.EXTERNAL_DATA_INCONSTANCY.getDescription());
        }
        return collection.iterator().next();
    }

}
