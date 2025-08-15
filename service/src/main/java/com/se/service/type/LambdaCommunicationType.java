package com.se.service.type;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Lambda communication types.
 */
public enum LambdaCommunicationType {

    SNS("sns", "Communication lambdas through the aws SNS"),
    LAMBDA_DIRECT("lambda-direct", "Direct communication for lambdas"),
    SNS_LOCAL("sns-local", "Communication lambdas through the aws SNS local"),
    LAMBDA_DIRECT_LOCAL("lambda-direct-local", "Direct communication for lambdas local");

    private static final Map<String, LambdaCommunicationType> LOOKUP = Arrays.stream(LambdaCommunicationType.values())
            .collect(Collectors.toMap(LambdaCommunicationType::getType, Function.identity()));

    private String type;
    private String description;

    /**
     * Constructor.
     *
     * @param type        of type communication.
     * @param description short description.
     */
    LambdaCommunicationType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static LambdaCommunicationType findByType(String type) {
        return Optional.ofNullable(type).map(LOOKUP::get).orElse(LambdaCommunicationType.LAMBDA_DIRECT);
    }
}
