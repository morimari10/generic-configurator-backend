package com.se.service.httpClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import com.se.common.DocHolder;
import com.se.common.ErrorCode;
import com.se.common.ExternalDataException;
import com.se.common.GlobalConstants;
import com.se.common.IntegrationUtil;
import com.se.utils.ConverterUtils;
import com.se.utils.model.CriteriaHolder;
import com.se.utils.model.ErrorHolder;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"unchecked", "rawtypes"})
public class HttpClientConverter {

    private String sncGlobalId;
    private Map<String, String> countryMapping;
    private Map<String, String> languageMapping;

    private static final String GLOBAL_COUNTRY_CODE = "WW";

    private static final List<String> GLOBAL_IDS = Arrays.asList("SCHINT", "GLOBAL");

    private static final Logger LOGGER = LogManager.getLogger(HttpClientConverter.class);

    private static final Integer WRONG_DATA_STATUS = 400;

    private static final String WRONG_DATA_CODE = "FUNC_003";

    private static final String DESCRIPTION_NOT_FOUND = "Description not found";

    /**
     * Constructor.
     *
     * @param sncGlobalId S&C global id.
     */
    public HttpClientConverter(String sncGlobalId) {
        this.sncGlobalId = sncGlobalId;
        countryMapping = ImmutableMap.<String, String>builder()
                .put("DZ", "DZA").put("AR", "ARG").put("AU", "AUS").put("AT", "AUT").put("BE", "BEL").put("BR", "BRA")
                .put("BG", "BGR").put("CA", "CAN").put("CL", "CHL").put("CN", "CHN").put("CO", "COL")
                .put("CR", "CRI").put("HR-HR", "HRV").put("CZ", "CZE").put("DK", "DNK")
                .put("EG", "EGY").put("AE", "ARE")
                .put("EE", "EST").put("FI", "FIN").put("FR", "FRA").put("DE", "DEU").put("GR", "GCR").put("HK", "HKG")
                .put("HU", "HUN").put("IN", "IND").put("ID", "IDN").put("IE", "IRL").put("IL", "ISR").put("IT", "ITA")
                .put("JP", "JPN").put("KZ", "KAZ").put("KR", "KOR").put("LV", "LVA").put("LT", "LTU").put("MY", "MYS")
                .put("MX", "MEX").put("MA", "MAR").put("NL", "NDL").put("NZ", "NZL").put("NG", "NGA").put("NO", "NOR")
                .put("PE", "PER").put("PH", "PHL").put("PL", "POL").put("PT", "PTR").put("RO", "ROU").put("RU", "RUS")
                .put("SA", "SAU").put("RS", "SRB").put("SG", "SGP").put("SK", "SVK").put("SI", "SVN").put("ZA", "ZAF")
                .put("ES", "ESP").put("SE", "SWE").put("CH", "CHE").put("TW", "TWN").put("TH", "THA").put("TR", "TUR")
                .put("UA", "UKR").put("UK", "GBR").put("GB", "GBR").put("US", "USA").put("VN", "VNM").put("XF", "FAF")
                .put(GLOBAL_COUNTRY_CODE, sncGlobalId)
                .build();

        languageMapping = ImmutableMap.<String, String>builder()
                .put("hr", "HR-HR")
                .put("el", "GR")
                .put("iw", "HE")
                .build();
    }

    /**
     * Extract ISO-2 country code from local and converts it to ISO-3 code according
     * to S&C scope. F.e.:
     * "fr-fr" -> "FR" -> "FRA"
     * "by-by" -> "BY" -not_in_scope-> "SCHINT"
     * "en-ww" -> "WW" -> "SCHINT"
     *
     * @param loc locale.
     * @return 3-letter ISO country code from locale according to S&C scope.
     */
    public String extractSnCCountry(String loc) {
        String country = IntegrationUtil.extractCountry(loc);

        if ("HR".equals(country)) {
            country = "HR-HR";
        }
        return Optional.ofNullable(countryMapping.get(country)).orElse(sncGlobalId);
    }

    /**
     * Extract language from locale and convert it to up-case.
     *
     * @param loc locale.
     * @return lag 2-letters code in uppercase.
     */
    String extractSnCLang(String loc) {
        Locale locale = new Locale.Builder().setLanguageTag(loc).build();
        return Optional.ofNullable(languageMapping.get(locale.getLanguage()))
                .map(s -> s.toUpperCase())
                .orElse(locale.getLanguage().toUpperCase());
    }

    /**
     * Smart find object from country mapping.
     *
     * @param countryMap country mapping.
     * @param loc        locale.
     * @param <T>        type of mapped objects.
     * @return found object by country.
     */
    <T> T findByCountryMapping(Map<String, T> countryMap, String loc) {
        String countryCode = extractSnCCountry(loc);
        if (countryMap.containsKey(countryCode)) {
            return countryMap.get(countryCode);
        }
        countryCode = sncGlobalId;
        if (countryMap.containsKey(countryCode)) {
            return countryMap.get(countryCode);
        }
        for (String code : GLOBAL_IDS) {
            if (countryMap.containsKey(code)) {
                return countryMap.get(code);
            }
        }
        return IntegrationUtil.getFirstItemCollection(countryMap.values());
    }

    /**
     * Extract docs from S&C response.
     *
     * @param jsonString json.
     * @return list of compiled doc holders.
     */
    public List<DocHolder> extractDocs(String jsonString) {
        CriteriaHolder criteriaHolder = Jackson.fromJsonString(jsonString, CriteriaHolder.class);
        if (criteriaHolder.getCriteriaList() == null) {
            ErrorHolder errorHolder = Jackson.fromJsonString(jsonString, ErrorHolder.class);
            LOGGER.debug("Error occurred while requesting S&C: {} ", errorHolder);
            if (WRONG_DATA_CODE.equals(errorHolder.getCode()) && WRONG_DATA_STATUS.equals(errorHolder.getStatus())) {
                return Collections.emptyList();
            }
            ErrorCode mccErrorCode = ErrorCode.EXTERNAL_DATA_INCONSTANCY;
            throw new ExternalDataException("Error occurs during converting documents", mccErrorCode.getHttpCode(),
                    mccErrorCode.getKey(), mccErrorCode.getDescription());
        }
        return criteriaHolder.getDocs();
    }

    @Data
    private static class DescriptionHolder {
        @JsonProperty("translation")
        private Map<String, String> translation;
    }

    @Data
    private static class Product {
        @JsonProperty("id")
        private String id;
        @JsonProperty("long_desc")
        private DescriptionHolder description;
    }

    @Data
    private static class ProductHolder {
        @JsonProperty("catalogue_product")
        private Product catalogueProduct;
    }

    private String getDescr(String loc, ProductHolder product) {
        String descr = product.getCatalogueProduct().getDescription().getTranslation().get(loc);
        if (StringUtils.isEmpty(descr)) {
            descr = product.getCatalogueProduct().getDescription().getTranslation().get(GlobalConstants.DEFAULT_DESCR_LOC);
        }
        return descr;
    }

    /**
     * Extract description from json response.
     *
     * @param descrJson json with descriptions.
     * @param loc       locale.
     * @return descrs map.
     */
    public Map<String, String> extractDescrs(String descrJson, String loc) {
        List descriptions;
        try {
            descriptions = Jackson.getObjectMapper().readValue(descrJson, List.class);
        } catch (IOException exception) {
            LOGGER.error("Occurred an error during parsing descriptions", exception);
            return Collections.emptyMap();
        }
        List<ProductHolder> product = ConverterUtils.convertListOfMapToPOJOList(ProductHolder.class,
                descriptions);
        if (CollectionUtils.isEmpty(product)) {
            return Collections.emptyMap();
        }
        return product.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ph -> ph.getCatalogueProduct().getId(), ph -> getDescr(loc, ph)));
    }

    /**
     * Extract description from json response.
     *
     * @param descrJson json with descriptions.
     * @return product holder list.
     */
    public List<com.se.common.ProductHolder> extractProductHolders(String descrJson) {
        List descriptions;
        try {
            descriptions = Jackson.getObjectMapper().readValue(descrJson, List.class);
        } catch (IOException exception) {
            LOGGER.error("Occurred an error during parsing descriptions", exception);
            return Collections.emptyList();
        }
        List<com.se.common.ProductHolder> product = ConverterUtils.convertListOfMapToPOJOList(com.se.common.ProductHolder.class,
                descriptions);
        if (CollectionUtils.isEmpty(product)) {
            return Collections.emptyList();
        }
        return product;
    }

        /**
     * Extract description from json response.
     *
     * @param descrJson json with description.
     * @param loc       locale.
     * @return descr.
     */
    public String extractDescr(String descrJson, String loc) {
        ProductHolder product = Jackson.fromJsonString(descrJson, ProductHolder.class);
        if (product.getCatalogueProduct() == null) {
            return DESCRIPTION_NOT_FOUND;
        }
        String descr = product.getCatalogueProduct().getDescription().getTranslation().get(loc);
        if (StringUtils.isEmpty(descr)) {
            descr = product.getCatalogueProduct().getDescription().getTranslation().get(GlobalConstants.DEFAULT_DESCR_LOC);
        }
        return descr;
    }
}
