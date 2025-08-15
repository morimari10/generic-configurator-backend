package com.se.common;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * layer for interaction with external systems.
 */
public final class ConstantSnC {

    /**
     * S&C Criteria range of product.
     */
    public static final String CRITERIA_PRODUCT_RANGE = "Range of product";
    /**
     * S&C Criteria product subset.
     */
    public static final String CRITERIA_PRODUCT_SUBSET = "ProductSubset";

    /**
     * S&C Coordination field catalog.
     */
    public static final String DOC_CATALOG = "Catalog";

    /**
     * Default value equals true.
     */
    public static final String YES_VALUE = "yes";
    /**
     * Default value equals false.
     */
    public static final String NO_VALUE = "no";

    /**
     * Mapping boolean values to string.
     */
    public static final Map<String, String> BOOLEAN_VALUES_TO_STRING_MAP = ImmutableMap.<String, String>builder()
        .put("true", YES_VALUE)
        .put("false", NO_VALUE)
        .build();

    private ConstantSnC() {
        throw new AssertionError("Constructor should not be called directly");
    }
}
