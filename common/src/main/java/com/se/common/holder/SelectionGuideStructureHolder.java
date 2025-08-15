package com.se.common.holder;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.se.common.deserializer.SelectionGuideStructureDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * It represents an MOMASelectionGuideStructureDeserializer parameter holder in the external system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = SelectionGuideStructureDeserializer.class)
public class SelectionGuideStructureHolder extends ConfigurationParameterHolder {
        private List<Map<String, String>> selectionGuideStructures;
}
