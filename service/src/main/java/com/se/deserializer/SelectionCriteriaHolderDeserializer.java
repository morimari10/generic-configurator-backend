package com.se.deserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.se.common.IntegrationUtil;
import com.se.common.deserializer.AbstractConfigParamDeserializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
public class SelectionCriteriaHolderDeserializer extends AbstractConfigParamDeserializer {

    private static final String SELECTION_CRITERIA = "SelectionCriteria";

    /**
     * Default constructor.
     */
    public SelectionCriteriaHolderDeserializer() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param clazz class
     */
    public SelectionCriteriaHolderDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public SelectionCriteriaHolder deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        Map<String, List<Map<String, String>>> paramMap = getParam(parser, SELECTION_CRITERIA);
        List<Map<String, String>> params = IntegrationUtil.getFirstItemCollection(paramMap.values());
        SelectionCriteriaHolder holder = new SelectionCriteriaHolder();
        holder.setParameters(params);
        return holder;
    }
    
}
