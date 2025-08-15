package com.se.domain.configuration.template;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Template types.
 */
public enum ProductType {
    /**
     * button type.
     */
    BUTTON("button"),
    /**
     * legend panel type.
     */
    LEGEND_PANEL("legend"),
    /**
     * plug type.
     */
    PLUG("plug"),
    /**
     * holder type.
     */
    HOLDER("holder"),
    /**
     * plate type.
     */
    PLATE("plate"),
    /**
     * switcher type.
     */
    SWITCHER("switcher"),
    /**
     * pocket type.
     */
    POCKET("pocket"),
    /**
     * legend ring type.
     */
    LEGEND_RING("legend ring"),
    /**
     * default type.
     */
    UNKNOWN("unknown");



    private static final Map<String, ProductType> LOOKUP = Arrays.stream(ProductType.values())
            .collect(Collectors.toMap(ProductType::getTemplateType, Function.identity()));

    private String productTypeName;

    /**
     * Constructor.
     *
     * @param productTypeName the template type name
     */
    ProductType(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    /**
     * Gets device name.
     *
     * @return device name
     */
    public String getTemplateType() {
        return productTypeName;
    }

    /**
     * Get enum element by the template type name.
     *
     * @param templateTypeName the template type name
     * @return enum element
     */
    public static ProductType get(String templateTypeName) {
        return LOOKUP.get(templateTypeName);
    }

    /**
     * Convert input string to {@link ProductType} object.
     *
     * @param text template type in string representation
     * @return template type or null
     */
    public static ProductType fromString(String text) {
        for (ProductType productType : ProductType.values()) {
            if (productType.name().equalsIgnoreCase(text)) {
                return productType;
            }
        }
        return null;
    }
}
