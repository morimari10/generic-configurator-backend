package com.se.common.holder;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.se.common.ReferencesInformation;
import com.se.common.deserializer.ReferencesInformationDeserializer;

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
@JsonDeserialize(using = ReferencesInformationDeserializer.class)
public class ReferencesInformationHolder extends ConfigurationParameterHolder {
        private List<ReferencesInformation> referencesInformation;
}
