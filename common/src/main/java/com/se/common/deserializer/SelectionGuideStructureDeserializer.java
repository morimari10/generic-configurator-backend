package com.se.common.deserializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.se.common.IntegrationUtil;
import com.se.common.holder.SelectionGuideStructureHolder;

public class SelectionGuideStructureDeserializer
        extends AbstractConfigParamTableDeserializer<SelectionGuideStructureHolder> {

    private static final String TABLE_NAME_PATTERN = "GENERIC-CONFIGURATORSelectionguideStructure";

    /**
     * Default constructor.
     */
    public SelectionGuideStructureDeserializer() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param clazz class
     */
    public SelectionGuideStructureDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public SelectionGuideStructureHolder deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException {
        Map<String, List<Map<String, String>>> paramMap = getParam(parser, TABLE_NAME_PATTERN);
        List<Map<String, String>> params = IntegrationUtil.getFirstItemCollection(paramMap.values());
        SelectionGuideStructureHolder holder = new SelectionGuideStructureHolder();
        holder.setParameters(params);
        return holder;
    }

    @Override
    public String getConfigTableName() {
        return TABLE_NAME_PATTERN;
    }

    @Override
    public SelectionGuideStructureHolder deserializeFromParams(List<Map<String, String>> params) {
        SelectionGuideStructureHolder holder = new SelectionGuideStructureHolder();
        holder.setParameters(params);
        return holder;
    }
}
