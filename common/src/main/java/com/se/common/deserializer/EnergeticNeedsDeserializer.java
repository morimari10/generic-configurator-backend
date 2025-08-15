package com.se.common.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.se.common.EnergeticNeeds;
import com.se.common.holder.EnergeticNeedsHolder;

public class EnergeticNeedsDeserializer extends AbstractConfigParamTableDeserializer<EnergeticNeedsHolder> {

    private static final String TABLE_NAME_PATTERN = "EnergeticNeeds";

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
    public EnergeticNeedsHolder deserializeFromParams(List<Map<String, String>> params) {
        EnergeticNeedsHolder holder = new EnergeticNeedsHolder();

        List<EnergeticNeeds> energeticNeedsList = new ArrayList<>();
        for (Map<String, String> param : params) {
            EnergeticNeeds energeticNeeds = new EnergeticNeeds();
            energeticNeeds.setTitle(param.remove(TITLE));
            energeticNeeds.setUiComponent(param.remove(UI_COMPONENT));
            energeticNeeds.setRank(this.getIntFromParam(param.remove(RANK)));
            energeticNeeds.setType(param.remove(TYPE));
            energeticNeeds.setFormat(param.remove(FORMAT));
            energeticNeeds.setCriteria(param.remove(CRITERIA));
            energeticNeeds.setMandatory(param.get(MANDATORY) != null ? Boolean.valueOf(param.get(MANDATORY)) : null);
            energeticNeedsList.add(energeticNeeds);
        }
        holder.setEnergeticNeeds(energeticNeedsList);

        return holder;
    }

}
