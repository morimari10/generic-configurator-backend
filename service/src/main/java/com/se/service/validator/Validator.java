package com.se.service.validator;

import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.se.common.ErrorCode;
import com.se.domain.exception.BadRequestException;

/**
 * Abstract validator.
 *
 * @param <T> validated object type.
 */
public abstract class Validator<T> {
    
    private static final Pattern UUID_PATTERN =
    Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
    private static final Logger LOGGER = LogManager.getLogger(Validator.class);
    
    /**
     * Checks if object is valid.
     *
     * @param object the validated object
     * @return true if object is valid, else false or throws an exception.
     */
    public abstract boolean validate(T object);

    /**
     * Checks if all objects are valid.
     *
     * @param objects the validated objects
     * @return true if all object passed the validation and false if at least one failed.
     */
    @SafeVarargs
    public final boolean validateAll(T... objects) {
        for (T object : objects) {
            if (!validate(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validate session identifier.
     *
     * @param sessionId the session identifier
     */
    public static void validateSessionId(String sessionId) {
        if (Objects.nonNull(sessionId) && !UUID_PATTERN.matcher(sessionId).matches()) {
            LOGGER.error("Error occurred during session identifier validation - [{}]", sessionId);
            ErrorCode errorCode = ErrorCode.SESSION_IS_NOT_VALID;
            throw new BadRequestException("Invalid GENERIC-CONFIGURATOR-Session format", errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }

}
