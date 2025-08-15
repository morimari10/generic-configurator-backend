package com.se.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.domain.configuration.Configuration;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JavaType;

import com.amazonaws.util.json.Jackson;

public class ConverterUtils {
    private static ObjectMapper objectMapper = Jackson.getObjectMapper();

    /**
     * Convert map to POJO.
     *
     * @param valueType the type to convert the object to
     * @param payload     map that should be converted
     * @param <T>       the type of the class
     * @return converted POJO
     */
    public static <T> Configuration convertMapToPOJO(Class<Configuration> valueType, Map<String, String> payload) {
        return objectMapper.convertValue(payload, valueType);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> List<T> convertListOfMapToPOJOList(Class<T> valueType, List<Map<String, Object>> value) {
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, valueType);
        return (List)objectMapper.convertValue(value, type);
    }

}
