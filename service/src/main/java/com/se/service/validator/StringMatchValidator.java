package com.se.service.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The string match validator.
 */
public class StringMatchValidator extends Validator<String> {

    private static final Logger LOGGER = LogManager.getLogger(StringMatchValidator.class);

    private String regexp;

    /**
     * Parametrized constructor.
     *
     * @param regexp the regexp for validating.
     */
    public StringMatchValidator(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean validate(String string) {
        if (!string.matches(regexp)) {
            LOGGER.info("String [{}] is not valid.", string);
            LOGGER.debug("String [{}] does not match regular expression [{}].", string, regexp);
            return false;
        }
        return true;
    }
}
