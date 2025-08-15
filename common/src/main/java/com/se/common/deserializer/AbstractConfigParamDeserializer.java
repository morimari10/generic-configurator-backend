package com.se.common.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.se.common.BooleanConverter;
import com.se.common.GlobalConstants;
import com.se.common.IntegrationUtil;
import com.se.common.holder.ConfigurationParameterHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Deserializer for {@link ConfigurationParameterHolder}.
 */
public abstract class AbstractConfigParamDeserializer extends StdDeserializer<ConfigurationParameterHolder> {

    /**
     * Default constructor.
     */
    public AbstractConfigParamDeserializer() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param clazz class
     */
    public AbstractConfigParamDeserializer(Class<?> clazz) {
        super(clazz);
    }

    /**
     * Get param from JSON by pattern.
     *
     * @param parser       parser.
     * @param paramPattern pattern.
     * @return param params.
     * @throws IOException exception.
     */
    protected List<Map<String, String>> getParams(JsonParser parser, String paramPattern) throws IOException {
        List<Map<String, String>> params = new ArrayList<>();
        JsonNode node = parser.getCodec().readTree(parser);
        Iterator<Map.Entry<String, JsonNode>> nodes = node.fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            if (entry.getKey().contains(paramPattern)) {
                Iterator<JsonNode> iterator = entry.getValue().elements();
                while (iterator.hasNext()) {
                    Map<String, String> paramMap = new LinkedHashMap<>();
                    JsonNode jsonNode = iterator.next();
                    Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> next = fields.next();
                        paramMap.put(next.getKey(), next.getValue().asText());
                    }
                    params.add(paramMap);
                }
            }
        }
        return params;
    }

    
    /**
     * Get param from JSON by pattern.
     *
     * @param parser       parser.
     * @param paramPattern pattern.
     * @return param params.
     * @throws IOException exception.
     */
    protected Map<String, List<Map<String, String>>> getParam(JsonParser parser, String paramPattern)
            throws IOException {
        Map<String, List<Map<String, String>>> mappedParams = new HashMap<>();
        JsonNode node = parser.getCodec().readTree(parser);
        Iterator<Map.Entry<String, JsonNode>> nodes = node.fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            String key = entry.getKey();
            if (key.contains(paramPattern)) {
                List<Map<String, String>> params = new ArrayList<>();
                Iterator<JsonNode> iterator = entry.getValue().elements();
                while (iterator.hasNext()) {
                    Map<String, String> paramMap = new HashMap<>();
                    JsonNode jsonNode = iterator.next();
                    Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> next = fields.next();
                        paramMap.put(next.getKey(), next.getValue().asText());
                    }
                    params.add(paramMap);
                }
                mappedParams.put(IntegrationUtil.extractCountryCodeFromConfig(key), params);
            }
        }
        return mappedParams;
    }

    /**
     * Get all possible parameter from JSON.
     *
     * @param parser parser
     * @return the parameter map
     * @throws IOException exception
     */
    protected Map<String, List<Map<String, String>>> extractParamsMap(JsonParser parser) throws IOException {
        Map<String, List<Map<String, String>>> paramsMap = new HashMap<>();
        JsonNode node = parser.getCodec().readTree(parser);
        Iterator<Map.Entry<String, JsonNode>> nodes = node.fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            Iterator<JsonNode> iterator = entry.getValue().elements();
            List<Map<String, String>> paramsList = new ArrayList<>();
            while (iterator.hasNext()) {
                Map<String, String> paramMap = new LinkedHashMap<>();
                JsonNode jsonNode = iterator.next();
                Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> next = fields.next();
                    paramMap.put(next.getKey(), next.getValue().asText());
                }
                paramsList.add(paramMap);
            }
            paramsMap.put(entry.getKey(), paramsList);
        }

        return paramsMap;
    }

    /**
     * Replace empty or N/A by a null value. Otherwise, the value itself is returned.
     *
     * @param paramValue the parameter value to be analyzed
     * @return the parameter value with null if it was an empty or N/A value
     */
    protected String asNull(String paramValue) {
        boolean isNullable = !Optional.ofNullable(paramValue)
            .filter(s -> !s.isEmpty())
            .filter(s -> !s.equals(GlobalConstants.NOT_APPLICABLE))
            .isPresent();
        return isNullable ? null : paramValue;
    }

    /**
     * Retrieve the parameter value as an Integer.
     *
     * @param paramValue the parameter value to be analyzed
     * @return the parameter value as nullable-integer
     */
    protected Integer getIntFromParam(String paramValue) {
        Integer value;
        try {
            value = Integer.parseInt(Optional.ofNullable(paramValue).orElse(""));
        } catch (NumberFormatException ignored) {
            value = null;
        }
        return value;
    }

    /**
     * Retrieve the parameter value as an int.
     *
     * @param paramValue   the parameter value to be analyzed
     * @param defaultValue the fallback value if null
     * @return the parameter value as integer
     */
    protected int getIntFromParam(String paramValue, int defaultValue) {
        return Optional.ofNullable(this.getIntFromParam(paramValue)).orElse(defaultValue);
    }

    /**
     * Retrieve the parameter value as an Double.
     *
     * @param paramValue the parameter value to be analyzed
     * @return the parameter value as nullable-double
     */
    protected Double getDoubleFromParam(String paramValue) {
        Double value;
        try {
            value = Double.parseDouble(Optional.ofNullable(paramValue).orElse(""));
        } catch (NumberFormatException ignored) {
            value = null;
        }
        return value;
    }

    /**
     * Retrieve the parameter value as an double.
     *
     * @param paramValue   the parameter value to be analyzed
     * @param defaultValue the fallback value if null
     * @return the parameter value as double
     */
    protected double getDoubleFromParam(String paramValue, double defaultValue) {
        return Optional.ofNullable(this.getDoubleFromParam(paramValue)).orElse(defaultValue);
    }

    /**
     * Retrieve the parameter value as an Float.
     *
     * @param paramValue the parameter value to be analyzed
     * @return the parameter value as nullable-float
     */
    protected Float getFloatFromParam(String paramValue) {
        Float value;
        try {
            value = Float.parseFloat(Optional.ofNullable(paramValue).orElse(""));
        } catch (NumberFormatException ignored) {
            value = null;
        }
        return value;
    }

    /**
     * Retrieve the parameter value as an float.
     *
     * @param paramValue   the parameter value to be analyzed
     * @param defaultValue the fallback value if null
     * @return the parameter value as float
     */
    protected float getFloatFromParam(String paramValue, float defaultValue) {
        return Optional.ofNullable(this.getFloatFromParam(paramValue)).orElse(defaultValue);
    }

    /**
     * Retrieve the parameter value as a boolean.
     *
     * @param paramValue the parameter value to be analyzed
     * @return the parameter value as boolean. If the parameter is null or not a boolean, it will return false.
     * @see Boolean#parseBoolean(String)
     */
    protected boolean getBoolFromParam(String paramValue) {
        return Boolean.parseBoolean(paramValue);
    }

    /**
     * Retrieve the parameter value as a nullable-boolean.
     *
     * @param paramValue the parameter value to be analyzed
     * @return the parameter value as nullable-boolean.
     * @see BooleanConverter#unconvert(String)
     */
    protected Boolean getBooleanFromParam(String paramValue) {
        return new BooleanConverter().unconvert(paramValue);
    }

    /**
     * Retrieve the parameter value as a boolean.
     *
     * @param paramValue   the parameter value to be analyzed
     * @param defaultValue the fallback value if null
     * @return the parameter value as boolean.
     * @see BooleanConverter#unconvert(String)
     */
    protected boolean getBooleanFromParam(String paramValue, boolean defaultValue) {
        return Optional.ofNullable(this.getBooleanFromParam(paramValue)).orElse(defaultValue);
    }

    /**
     * Retrieve the parameter value as a string list.
     *
     * @param paramValue the parameter value to be analyzed
     * @return the parameter value as string list. If the parameter was null, it will return an empty array.
     */
    protected List<String> getListFromParam(String paramValue) {
        return Optional.ofNullable(paramValue)
            .map(param -> param.split("\\|\\|"))
            .map(Arrays::asList)
            .orElseGet(ArrayList::new);
    }

    /**
     * Retrieve the parameter value as a string list.
     *
     * @param paramValue the parameter value to be analyzed
     * @return the parameter value as string list. If the parameter was null, it will return an empty array.
     */
    protected List<String> getListFromSemicolumnParam(String paramValue) {
        return Optional.ofNullable(paramValue)
            .map(param -> param.split(";"))
            .map(Arrays::asList)
            .orElseGet(ArrayList::new);
    }
}
