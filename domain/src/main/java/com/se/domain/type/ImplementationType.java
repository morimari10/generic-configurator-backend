package com.se.domain.type;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation type enum.
 */
public enum ImplementationType {

    AWS("aws", "Use remote aws services"),
    LOCAL("local", "Use local aws services");

    private static final Map<String, ImplementationType> LOOKUP = Arrays.stream(ImplementationType.values())
            .collect(Collectors.toMap(ImplementationType::getType, Function.identity()));

    private String type;
    private String description;

    /**
     * Constructor.
     *
     * @param type        of type communication.
     * @param description short description.
     */
    ImplementationType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static ImplementationType findByType(String type) {
        return Optional.ofNullable(type).map(LOOKUP::get).orElse(ImplementationType.AWS);
    }
}
