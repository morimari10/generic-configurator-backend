package com.se.common.holder;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.se.common.Structure;
import com.se.common.deserializer.StructureDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * It represents an MOMACountryURLsMappingDeserializer parameter holder in the external system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = StructureDeserializer.class)
public class StructureHolder extends ConfigurationParameterHolder {
        private List<Structure> structures;
}
