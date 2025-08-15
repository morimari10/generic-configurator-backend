package com.se.common.deserializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.se.common.CountryURLsMapping;
import com.se.common.holder.CountryURLsMappingHolder;


public class CountryURLsMappingDeserializer extends AbstractConfigParamTableDeserializer<CountryURLsMappingHolder> {

    private static final String TABLE_NAME_PATTERN = "MCCCountryURLsMapping";

    private static final String LOCALE = "Locale";
    private static final String COUNTRY_CODE = "Country Code";
    private static final String LANGUAGE = "Language";
    private static final String OPS_CODE = "OPS Code";
    private static final String COUNTRY = "Country";

    @Override
    public String getConfigTableName() {
        return TABLE_NAME_PATTERN;
    }

    @Override
    public CountryURLsMappingHolder deserializeFromParams(List<Map<String, String>> params) {
        CountryURLsMappingHolder holder = new CountryURLsMappingHolder();

        List<CountryURLsMapping> countryURLsMappings = new ArrayList<>();
        for (Map<String, String> param : params) {
            CountryURLsMapping countryURLsMapping = new CountryURLsMapping();
            countryURLsMapping.setLocale(param.remove(LOCALE));
            countryURLsMapping.setCountryCode(param.remove(COUNTRY_CODE));
            countryURLsMapping.setLanguage(param.remove(LANGUAGE));
            countryURLsMapping.setOpsCode(param.remove(OPS_CODE));
            countryURLsMapping.setCountry(param.remove(COUNTRY));
            countryURLsMappings.add(countryURLsMapping);
        }
        holder.setCountryURLsMappings(countryURLsMappings);

        return holder;
    }
    
}
