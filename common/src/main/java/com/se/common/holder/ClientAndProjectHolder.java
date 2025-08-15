package com.se.common.holder;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.se.common.ClientAndProject;
import com.se.common.deserializer.ClientAndProjectDeserializer;

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
@JsonDeserialize(using = ClientAndProjectDeserializer.class)
public class ClientAndProjectHolder extends ConfigurationParameterHolder {
        private List<ClientAndProject> clientAndProjects;
}
