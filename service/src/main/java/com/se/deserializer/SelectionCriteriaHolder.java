package com.se.deserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.se.common.holder.ConfigurationParameterHolder;

/**
 * The type Country parameter holder.
 */
@JsonDeserialize(using = SelectionCriteriaHolderDeserializer.class)
public class SelectionCriteriaHolder extends ConfigurationParameterHolder {
}
