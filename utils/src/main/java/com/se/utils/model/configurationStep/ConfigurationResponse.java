package com.se.utils.model.configurationStep;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.common.ClientAndProject;
import com.se.common.ConfigurationElement;
import com.se.common.SelectionCriterias;
import com.se.common.Structure;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ConfigurationResponse {
    private Map<String, SelectionCriterias> selectionCriterias;
    private List<Structure> structure;
    private List<ConfigurationElement> configurationElements;
    private Boolean warningMessage;
    private String warningMessageText;
    private int layersToRemove;
    private List<ConfigurationElement> possibleConfigurationElementsToRemove;
    private List<String> references;
    private String incompatibleReferenceProductType;
    private ConfigurationElement incompatibleReference;
    private List<ClientAndProject> clientAndProjects;

    public ConfigurationResponse() {
    }

    public ConfigurationResponse(Map<String, SelectionCriterias> selectionCriterias) {
        this.selectionCriterias = selectionCriterias;
    }
}
