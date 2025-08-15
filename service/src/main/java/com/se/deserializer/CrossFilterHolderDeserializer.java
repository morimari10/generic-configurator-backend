package com.se.deserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.se.common.IntegrationUtil;
import com.se.common.deserializer.AbstractConfigParamDeserializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
public class CrossFilterHolderDeserializer extends AbstractConfigParamDeserializer {

    private static final String CROSS_FILTER = "CrossFilter";

    /**
     * Default constructor.
     */
    public CrossFilterHolderDeserializer() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param clazz class
     */
    public CrossFilterHolderDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public CrossFilterHolder deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        Map<String, List<Map<String, String>>> paramMap = getParam(parser, CROSS_FILTER);
        List<Map<String, String>> params = IntegrationUtil.getFirstItemCollection(paramMap.values());
        CrossFilterHolder holder = new CrossFilterHolder();
        holder.setParameters(params);
        return holder;
    }
    
}
