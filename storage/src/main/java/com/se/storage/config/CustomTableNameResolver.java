package com.se.storage.config;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.Map;
import java.util.Optional;

/**
 * Table name resolver for adding prefix to table name.
 * If prefix is empty, no prefix added,
 * else strings are concatenated: prefix + '.' + table_name
 */
public class CustomTableNameResolver extends DynamoDBMapperConfig.DefaultTableNameResolver {
    private final String prefix;
    private final String postfix;

    /**
     * Constructor.
     *
     * @param prefix  prefix.
     * @param postfix postfix
     */
    public CustomTableNameResolver(String prefix, String postfix) {
        this.prefix = prefix;
        this.postfix = postfix;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public String getTableName(Class clazz, DynamoDBMapperConfig config) {
        String baseName = super.getTableName(clazz, config);
        String template = "${prefix}${baseName}${postfix}";
        Map<String, String> replaceMap = ImmutableMap.<String, String>builder()
                .put("baseName", baseName)
                .put("prefix", Optional.ofNullable(prefix).orElse(StringUtils.EMPTY))
                .put("postfix", Optional.ofNullable(postfix).orElse(StringUtils.EMPTY))
                .build();
        return StringSubstitutor.replace(template, replaceMap);
    }
}
