package com.se.service.httpClient;

import java.util.List;
import java.util.Map;

import com.se.common.DocHolder;
import com.se.common.SelectionCriteria;

public interface HttpClient {

    String getMainTableContent(String mainGuideValue, String locale);

    List<DocHolder> getSelectionGuideReferences(String mainGuideValue, String locale);

    String getDescription(String crId, String locale);

    List<DocHolder> getSelectionGuide(String mainGuideValue, String locale, String productType);

    List<DocHolder> getSelection(List<SelectionCriteria> selectionCriterias, String mainGuideValue, String locale,
            String productType, Map<String, List<DocHolder>> selectionMap);

    /**
     * Get all value configuration tables.
     *
     * @param selectionGuide selectionGuide
     * @param locale         locale
     * @return response configuration S&C values.
     */
    String getConfiguration(String selectionGuide, String locale);
}
