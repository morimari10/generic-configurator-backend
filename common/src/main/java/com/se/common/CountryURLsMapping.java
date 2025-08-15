package com.se.common;

import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CountryURLsMapping {
    private String locale;
    private String countryCode;
    private String language;
    private String opsCode;
    private String country;
}
