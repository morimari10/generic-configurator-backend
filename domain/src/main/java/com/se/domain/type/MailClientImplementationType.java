package com.se.domain.type;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mail client implementation types.
 */
public enum MailClientImplementationType {

    AWS("aws", "Amazon mail client implementation"),
    APACHE("apache", "Apache mail client implementation"),
    LOCAL("local", "Local mail client implementation");

    private static final Map<String, MailClientImplementationType> LOOKUP =
            Arrays.stream(MailClientImplementationType.values())
            .collect(Collectors.toMap(MailClientImplementationType::getType, Function.identity()));

    private String type;
    private String description;

    /**
     * Constructor.
     *
     * @param type        of type communication.
     * @param description short description.
     */
    MailClientImplementationType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static MailClientImplementationType findByType(String type) {
        return Optional.ofNullable(type).map(LOOKUP::get).orElse(MailClientImplementationType.APACHE);
    }
}
