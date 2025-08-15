package com.se.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.se.common.ConstantSnC;
import com.se.common.GlobalConstants;

import java.util.Optional;

/**
 * Converter for Boolean type.
 */
public class BooleanConverter implements DynamoDBTypeConverter<String, Boolean> {

    @Override
    public String convert(Boolean object) {
        return Optional.ofNullable(object)
            .map(Object::toString)
            .map(ConstantSnC.BOOLEAN_VALUES_TO_STRING_MAP::get)
            .orElse(GlobalConstants.NOT_APPLICABLE);
    }

    @Override
    public Boolean unconvert(String object) {
        return Optional.ofNullable(object)
            .filter(s -> ConstantSnC.YES_VALUE.equalsIgnoreCase(s) || ConstantSnC.NO_VALUE.equalsIgnoreCase(s))
            .map(ConstantSnC.YES_VALUE::equalsIgnoreCase)
            .orElse(null);
    }
}
