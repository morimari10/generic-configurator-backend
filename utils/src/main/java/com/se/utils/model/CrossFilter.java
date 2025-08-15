package com.se.utils.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CrossFilter {
    private String productType;
    private String source;
    private String sourceColumn;
    private String columnToFilter;
    private List<String> values;
}
