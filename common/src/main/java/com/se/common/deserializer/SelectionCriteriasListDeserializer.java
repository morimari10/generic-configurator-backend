package com.se.common.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.se.common.SelectionCriteria;
import com.se.common.SelectionCriterias;
import com.se.common.holder.SelectionCriteriasListHolder;

public class SelectionCriteriasListDeserializer
        extends AbstractConfigParamTableDeserializer<SelectionCriteriasListHolder> {

    private static final String TABLE_NAME_PATTERN = "SelectionCriteria";

    private static final String PRODUCT_TYPE = "ProductType";
    private static final String CRITERIA = "Criteria";
    private static final String LABEL = "Label";
    private static final String RANKING = "Ranking";
    private static final String UI_COMPONENT = "UI component";
    private static final String GLOBAL_RANKING = "GlobalRanking";
    private static final String INFO_TIP = "Info tip";
    private static final String DEFAULT_VALUE = "Default value";
    private static final String MAIN_SELECTION = "Main selection";
    private static final String INFO_TIP_PER_AVAILABLE_VALUE = "Info tip per available value";

    @Override
    public String getConfigTableName() {
        return TABLE_NAME_PATTERN;
    }

    @Override
    public SelectionCriteriasListHolder deserializeFromParams(List<Map<String, String>> params) {
        SelectionCriteriasListHolder holder = new SelectionCriteriasListHolder();

        Map<String, List<Map<String, String>>> groupedParams = new java.util.HashMap<>();
        for (Map<String, String> param : params) {
            String productType = param.get(PRODUCT_TYPE);
            groupedParams.computeIfAbsent(productType, k -> new ArrayList<>()).add(param);
        }

        List<SelectionCriterias> selectionCriteriasList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, String>>> entry : groupedParams.entrySet()) {
            List<Map<String, String>> group = entry.getValue();

            List<SelectionCriteria> selectionCriteriaList = new ArrayList<>();
            String globalRanking = null;
            List<String> mainSelection = null;
            for (Map<String, String> param : group) {
                SelectionCriteria selectionCriteria = new SelectionCriteria();
                selectionCriteria.setProductType(param.get(PRODUCT_TYPE));
                selectionCriteria.setCriteria(param.get(CRITERIA));
                selectionCriteria.setLabel(param.get(LABEL));
                selectionCriteria.setRanking(param.get(RANKING));
                selectionCriteria.setUiComponent(param.get(UI_COMPONENT));
                selectionCriteria.setInfoTip(param.get(INFO_TIP));
                selectionCriteria.setDefaultValue(param.get(DEFAULT_VALUE));
                selectionCriteria.setInfoTipPerAvailableValue(param.get(INFO_TIP_PER_AVAILABLE_VALUE) != null ? Boolean.valueOf(param.get(INFO_TIP_PER_AVAILABLE_VALUE)) : null);
                selectionCriteriaList.add(selectionCriteria);
                if (globalRanking == null) globalRanking = param.get(GLOBAL_RANKING);
                if (mainSelection == null && param.get(MAIN_SELECTION) != null) {
                    mainSelection = java.util.Arrays.asList(param.get(MAIN_SELECTION).split(","));
                }
            }
            SelectionCriterias selectionCriterias = new SelectionCriterias();
            selectionCriterias.setSelectionCriteria(selectionCriteriaList);
            selectionCriterias.setGlobalRanking(globalRanking);
            selectionCriterias.setMainSelection(mainSelection);
            selectionCriteriasList.add(selectionCriterias);
        }
        holder.setSelectionCriteriasList(selectionCriteriasList);
        return holder;
    }

}
