package com.se.common.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.se.common.SiteAndInstallation;
import com.se.common.holder.SiteAndInstallationHolder;

public class SiteAndInstallationDeserializer extends AbstractConfigParamTableDeserializer<SiteAndInstallationHolder> {

    private static final String TABLE_NAME_PATTERN = "StieAndInstallation";

    private static final String TITLE = "Title";
    private static final String UI_COMPONENT = "UI component";
    private static final String RANK = "Rank";
    private static final String TYPE = "Type";
    private static final String FORMAT = "Format";
    private static final String MANDATORY = "Mandatory";
    private static final String CRITERIA = "Criteria";

    @Override
    public String getConfigTableName() {
        return TABLE_NAME_PATTERN;
    }

    @Override
    public SiteAndInstallationHolder deserializeFromParams(List<Map<String, String>> params) {
        SiteAndInstallationHolder holder = new SiteAndInstallationHolder();

        List<SiteAndInstallation> siteAndInstallations = new ArrayList<>();
        for (Map<String, String> param : params) {
            SiteAndInstallation siteAndInstallation = new SiteAndInstallation();
            siteAndInstallation.setTitle(param.remove(TITLE));
            siteAndInstallation.setUiComponent(param.remove(UI_COMPONENT));
            siteAndInstallation.setRank(this.getIntFromParam(param.remove(RANK)));
            siteAndInstallation.setType(param.remove(TYPE));
            siteAndInstallation.setFormat(param.remove(FORMAT));
            siteAndInstallation.setCriteria(param.remove(CRITERIA));
            siteAndInstallation.setMandatory(param.get(MANDATORY) != null ? Boolean.valueOf(param.get(MANDATORY)) : null);
            siteAndInstallations.add(siteAndInstallation);
        }
        holder.setSiteAndInstallations(siteAndInstallations);

        return holder;
    }

}
