package com.se.common.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.se.common.holder.ConfigurationParameterHolder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Deserializer for {@link ConfigurationParameterHolder}.
 *
 * @param <T> the data holder type extending {@link ConfigurationParameterHolder}
 */
public abstract class AbstractConfigParamTableDeserializer<T extends ConfigurationParameterHolder>
    extends AbstractConfigParamDeserializer {

    /**
     * Default constructor.
     */
    public AbstractConfigParamTableDeserializer() {
        super();
    }

    /**
     * Constructor.
     *
     * @param clazz class
     */
    public AbstractConfigParamTableDeserializer(Class<?> clazz) {
        super(clazz);
    }

    /**
     * Get the configuration table name.
     *
     * @return the configuration table name
     */
    public abstract String getConfigTableName();

    /**
     * Method to deserialize params, used by main selection guide.
     *
     * @param params params
     * @return the configuration parameter holder
     */
    public abstract T deserializeFromParams(List<Map<String, String>> params);

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        List<Map<String, String>> params = this.getParams(p, this.getConfigTableName());
        return this.deserializeFromParams(params);
    }
}
