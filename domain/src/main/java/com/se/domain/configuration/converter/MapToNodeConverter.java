package com.se.domain.configuration.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converter between {@link Map} and {@link JsonNode} types.
 */
public class MapToNodeConverter implements DynamoDBTypeConverter<Map<String, AttributeValue>, JsonNode> {

    @Override
    public Map<String, AttributeValue> convert(JsonNode input) {
        Map<String, AttributeValue> map = new LinkedHashMap<>();
        input.fields().forEachRemaining(entry -> {
            Map<String, AttributeValue> singleMap =
                    ItemUtils.toAttributeValues(new Item().withJSON(entry.getKey(), entry.getValue().toString()));
            map.putAll(singleMap);
        });
        return map;
    }

    @Override
    public JsonNode unconvert(Map<String, AttributeValue> input) {
        try {
            return Jackson.getObjectMapper().readTree(ItemUtils.toItem(input).toJSON());
        } catch (IOException ex) {
            return NullNode.getInstance();
        }
    }
}

