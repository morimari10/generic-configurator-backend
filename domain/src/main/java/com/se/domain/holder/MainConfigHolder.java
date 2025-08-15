package com.se.domain.holder;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.se.common.ClientAndProject;
import com.se.common.CountryURLsMapping;
import com.se.common.EnergeticNeeds;
import com.se.common.ReferencesInformation;
import com.se.common.SelectionCriterias;
import com.se.common.SiteAndInstallation;
import com.se.common.Structure;
import com.se.common.holder.ConfigurationParameterHolder;
import com.se.domain.deserializer.MainConfigDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * It represents the main plc configuration object in the external system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = MainConfigDeserializer.class)
public class MainConfigHolder extends ConfigurationParameterHolder {
    private List<CountryURLsMapping> countryURLsMappings;
    private List<Map<String, String>> selectionGuideStructures;
    private List<SelectionCriterias> selectionCriteriasList;
    private List<Structure> structures;
    private List<ClientAndProject> clientAndProjects;
    private List<SiteAndInstallation> siteAndInstallations;
    private List<ReferencesInformation> referencesInformation;
    private List<EnergeticNeeds> energeticNeeds;
}
