package com.se.domain.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.se.common.deserializer.AbstractConfigParamDeserializer;
import com.se.common.deserializer.AbstractConfigParamTableDeserializer;
import com.se.common.deserializer.ClientAndProjectDeserializer;
import com.se.common.deserializer.CountryURLsMappingDeserializer;
import com.se.common.deserializer.EnergeticNeedsDeserializer;
import com.se.common.deserializer.ReferencesInformationDeserializer;
import com.se.common.deserializer.SelectionCriteriasListDeserializer;
import com.se.common.deserializer.SelectionGuideStructureDeserializer;
import com.se.common.deserializer.SiteAndInstallationDeserializer;
import com.se.common.deserializer.StructureDeserializer;
import com.se.common.holder.ConfigurationParameterHolder;
import com.se.domain.holder.MainConfigHolder;

public class MainConfigDeserializer extends AbstractConfigParamDeserializer {

    @Override
    public MainConfigHolder deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        MainConfigHolder holder = new MainConfigHolder();

        Map<String, List<Map<String, String>>> paramsMap = this.extractParamsMap(p);

        holder.setCountryURLsMappings(
                this.getHolder(new CountryURLsMappingDeserializer(), paramsMap)
                        .getCountryURLsMappings());

        holder.setSelectionGuideStructures(
                this.getHolder(new SelectionGuideStructureDeserializer(), paramsMap)
                        .getSelectionGuideStructures());

        holder.setSelectionCriteriasList(
                this.getHolder(new SelectionCriteriasListDeserializer(), paramsMap)
                        .getSelectionCriteriasList());

        holder.setStructures(
                this.getHolder(new StructureDeserializer(), paramsMap)
                        .getStructures());

        holder.setClientAndProjects(
                this.getHolder(new ClientAndProjectDeserializer(), paramsMap)
                        .getClientAndProjects());

        holder.setSiteAndInstallations(
                this.getHolder(new SiteAndInstallationDeserializer(), paramsMap)
                        .getSiteAndInstallations());

        holder.setReferencesInformation(
                this.getHolder(new ReferencesInformationDeserializer(), paramsMap)
                        .getReferencesInformation());

        holder.setEnergeticNeeds(
                this.getHolder(new EnergeticNeedsDeserializer(), paramsMap)
                        .getEnergeticNeeds());

        return holder;
    }

    private <T extends ConfigurationParameterHolder> T getHolder(AbstractConfigParamTableDeserializer<T> deserializer,
            Map<String, List<Map<String, String>>> paramsMap) {
        List<Map<String, String>> deserializerParams = paramsMap.get(deserializer.getConfigTableName());
        return deserializer.deserializeFromParams(Optional.ofNullable(deserializerParams).orElseGet(ArrayList::new));
    }

}
