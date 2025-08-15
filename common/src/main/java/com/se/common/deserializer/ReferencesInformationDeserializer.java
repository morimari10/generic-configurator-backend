package com.se.common.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.se.common.ReferencesInformation;
import com.se.common.holder.ReferencesInformationHolder;

public class ReferencesInformationDeserializer
        extends AbstractConfigParamTableDeserializer<ReferencesInformationHolder> {

    private static final String TABLE_NAME_PATTERN = "ReferencesInformation";

    private static final String CATALOG = "Catalog";
    private static final String YIELD = "Yield";
    private static final String PRICE = "Price";

    @Override
    public String getConfigTableName() {
        return TABLE_NAME_PATTERN;
    }

    @Override
    public ReferencesInformationHolder deserializeFromParams(List<Map<String, String>> params) {
        ReferencesInformationHolder holder = new ReferencesInformationHolder();

        List<ReferencesInformation> referencesInformationList = new ArrayList<>();
        for (Map<String, String> param : params) {
            ReferencesInformation referencesInformation = new ReferencesInformation();
            referencesInformation.setCatalog(param.remove(CATALOG));
            referencesInformation.setYield(param.remove(YIELD));
            referencesInformation.setPrice(this.getIntFromParam(param.remove(PRICE)));
            referencesInformationList.add(referencesInformation);
        }
        holder.setReferencesInformation(referencesInformationList);

        return holder;
    }
}
