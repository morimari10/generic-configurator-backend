package com.se.domain.type;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TranslationServiceImplementationType {

    SDL("sdl", "As a source master translation file are sld system"),
    S3("s3", "As a source master translation file are S3");

    private static final Map<String, TranslationServiceImplementationType> LOOKUP =
            Arrays.stream(TranslationServiceImplementationType.values())
            .collect(Collectors.toMap(TranslationServiceImplementationType::getType, Function.identity()));

    private String type;
    private String description;

    /**
     * Constructor.
     *
     * @param type        of type communication.
     * @param description short description.
     */
    TranslationServiceImplementationType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static TranslationServiceImplementationType findByType(String type) {
        return Optional.ofNullable(type).map(LOOKUP::get).orElse(TranslationServiceImplementationType.SDL);
    }
}
