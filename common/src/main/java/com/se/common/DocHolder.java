package com.se.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * It represent a criteria in the external system.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class DocHolder {
    @JsonProperty("results")
    private List<Document> results;

    public String getValue(String name) {
        for (Document document : this.results) {
            if (document.getName().equalsIgnoreCase(name)) {
                return document.getValue();
            }
        }
        return null;
    }
}
