package com.se.deserializer;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.se.common.Structure;
import com.se.common.holder.ConfigurationParameterHolder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * It represents an PanelStructureDeserializer parameter holder in the external system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = PanelStructureDeserializer.class)
public class PanelStructureHolder extends ConfigurationParameterHolder {
        private List<Structure> structures;
}
