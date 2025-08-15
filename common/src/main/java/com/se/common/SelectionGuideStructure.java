package com.se.common;

import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SelectionGuideStructure {
    private String country;
    private String momaFile;
}
