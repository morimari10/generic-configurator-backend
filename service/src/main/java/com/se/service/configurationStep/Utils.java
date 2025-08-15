package com.se.service.configurationStep;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.se.common.AvailableValue;
import com.se.common.ConfigurationElement;
import com.se.common.DocHolder;
import com.se.common.Document;
import com.se.common.GlobalConstants;
import com.se.common.SelectionCriteria;
import com.se.common.SelectionCriterias;
import com.se.deserializer.SelectionCriteriaHolder;
import com.se.service.httpClient.HttpClient;
import com.se.utils.model.CrossFilter;
import com.se.utils.model.configurationStep.ConfigurationResponse;

@SuppressWarnings("PMD.UselessParentheses")
public class Utils {

    private static final int THREAD_POOL_SIZE = 10;

    public static final Map<String, String> LOCALE_TO_SCOPE = ImmutableMap.<String, String>builder()
            .put("sl-SL", "NG")
            .put("en-UK", "GB")
            .build();

    public static boolean isNa(String value) {
        return Objects.equals(value, GlobalConstants.N_A);
    }

    public static String getScope(String locale) {
        return new Locale.Builder().setLanguageTag(locale).build().getCountry();
    }

    public static boolean allSelectionCriteriaSelected(List<SelectionCriteria> selectionCriterias) {
        for (SelectionCriteria selectionCriteria : selectionCriterias) {
            if (Objects.isNull(selectionCriteria.getSelectedValue())
                    || isNa(selectionCriteria.getSelectedValue())) {
                return false;
            }
        }
        return true;
    }

    public static Boolean configurationElementsContainsReference(List<ConfigurationElement> configurationElements,
            String reference) {
        for (ConfigurationElement configurationElement : configurationElements) {
            if (configurationElement.getCatalog().equalsIgnoreCase(reference)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isModuleReference(List<ConfigurationElement> configurationElements, String reference) {
        for (ConfigurationElement configurationElement : configurationElements) {
            if (configurationElement.getCatalog().equalsIgnoreCase(reference)
                    && (configurationElement.getProductType().equalsIgnoreCase(GlobalConstants.DATA_TOP)
                            || configurationElement.getProductType().equalsIgnoreCase(GlobalConstants.DATA_LENS))) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getAllReferences(List<DocHolder> docHolders) {
        Set<String> references = new HashSet<>();
        for (DocHolder docHolder : docHolders) {
            for (Document document : docHolder.getResults()) {
                if (document.getName().equals(GlobalConstants.DATA_CATALOG)
                        && !isNa(document.getValue())) {
                    String[] catalogArray = document.getValue().split(";");
                    List<String> refs = new ArrayList<>();
                    for (String cat : catalogArray) {
                        refs.add(cat.trim());
                    }
                    references.addAll(refs);
                }
            }
        }
        return new ArrayList<>(references);
    }

    public static List<String> getAllReferencesWithShadow(List<DocHolder> docHolders) {
        Set<String> references = new HashSet<>();
        for (DocHolder docHolder : docHolders) {
            for (Document document : docHolder.getResults()) {
                if ((document.getName().equals(GlobalConstants.DATA_CATALOG))
                        && !isNa(document.getValue())) {
                    String[] catalogArray = document.getValue().split(";");
                    List<String> refs = new ArrayList<>();
                    for (String cat : catalogArray) {
                        refs.add(cat.trim());
                    }
                    references.addAll(refs);
                }
            }
        }
        return new ArrayList<>(references);
    }

    public static List<String> getAllGroupedReferences(List<DocHolder> docHolders) {
        Set<String> references = new HashSet<>();
        for (DocHolder docHolder : docHolders) {
            for (Document document : docHolder.getResults()) {
                if (document.getName().equals(GlobalConstants.DATA_CATALOG)
                        && !isNa(document.getValue())) {
                    references.add(document.getValue());
                }
            }
        }
        return new ArrayList<>(references);
    }

    public static Map<String, List<DocHolder>> getSelectionGuideParallel(List<String> allProductTypes,
            HttpClient httpClient,
            String main,
            String locale) throws InterruptedException, ExecutionException {
        String mainTableContent = httpClient.getMainTableContent(main, locale);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        Map<String, List<DocHolder>> selectionMap = new ConcurrentHashMap<>();
        List<Future<Void>> futures = new ArrayList<>();
        for (String productType : allProductTypes) {
            futures.add(executorService.submit(() -> {
                List<DocHolder> selection = httpClient.getSelectionGuide(
                        mainTableContent,
                        locale,
                        productType);
                selectionMap.put(productType, selection);
                return null;
            }));
        }
        for (Future<Void> future : futures) {
            future.get();
        }
        executorService.shutdown();
        return selectionMap;
    }

    public static List<String> getAllProductTypes(HttpClient httpClient, String main, String locale, String diameter) {
        String mainTableContent = httpClient.getMainTableContent(main, locale);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setInjectableValues(new InjectableValues.Std().addValue("diameter", diameter));
        SelectionCriteriaHolder holder = null;
        try {
            holder = mapper.readValue(mainTableContent, SelectionCriteriaHolder.class);
        } catch (IOException e) {

            e.printStackTrace();
        }
        List<Map<String, String>> selectionCriteria = holder.getParameters();
        Set<String> productTypes = new HashSet<>();
        for (Map<String, String> selection : selectionCriteria) {
            productTypes.add(selection.get(GlobalConstants.DATA_PRODUCT_TYPE));
        }
        return new ArrayList<>(productTypes);
    }

    public static List<String> getCbrReferencesToUse(List<String> cbrReferences, List<DocHolder> docHolders) {
        List<String> cbrReferencesToUse = new ArrayList<>();
        if (Objects.nonNull(cbrReferences) && cbrReferences.size() > 0) {
            List<String> references = Utils.getAllReferencesWithShadow(docHolders);
            for (String cbrReference : cbrReferences) {
                if (references.contains(cbrReference)) {
                    cbrReferencesToUse.add(cbrReference);
                }
            }
        }
        return cbrReferencesToUse;
    }

    public static Map<String, String> getCbrReferencesMapToUse(List<String> cbrReferences,
            List<DocHolder> docHolders, List<SelectionCriteria> selectionCriterias) {
        Map<String, String> cbrReferencesToUse = new HashMap<>();
        if (Objects.nonNull(cbrReferences) && cbrReferences.size() > 0) {
            for (String reference : cbrReferences) {
                for (DocHolder docHolder : docHolders) {
                    if (checkDocumentMatch(docHolder, selectionCriterias, null)) {
                        for (Document document : docHolder.getResults()) {
                            if (document.getValue().equalsIgnoreCase(reference)) {
                                cbrReferencesToUse.put(document.getValue(), document.getName());
                            }
                        }
                    }
                }
            }
        }
        return cbrReferencesToUse;
    }

    public static boolean checkDocumentMatch(DocHolder docHolder, List<SelectionCriteria> selectionCriterias,
            List<String> cbrRanges) {
        if (Objects.isNull(selectionCriterias)) {
            return true;
        }
        for (SelectionCriteria selectionCriteria : selectionCriterias) {
            for (Document document : docHolder.getResults()) {
                if (Objects.nonNull(selectionCriteria.getSelectedValue())
                        && !isNa(selectionCriteria.getSelectedValue())
                        && document.getName().equals(selectionCriteria.getCriteria())
                        && !selectionCriteria.getSelectedValue().equals(document.getValue())) {
                    return false;
                }
            }
        }
        for (Document document : docHolder.getResults()) {
            if (document.getName().equals(GlobalConstants.DATA_RANGE)
                    && Objects.nonNull(cbrRanges) && !cbrRanges.isEmpty()
                    && !isNa(document.getValue()) && !cbrRanges.contains(document.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkDocumentMatch(DocHolder docHolder, List<SelectionCriteria> selectionCriterias,
            List<String> cbrRanges, List<String> cbrReferences, Boolean checkAllReferences) {
        if (Objects.isNull(selectionCriterias)) {
            return true;
        }
        for (SelectionCriteria selectionCriteria : selectionCriterias) {
            for (Document document : docHolder.getResults()) {
                if (Objects.nonNull(selectionCriteria.getSelectedValue())
                        && !isNa(selectionCriteria.getSelectedValue())
                        && document.getName().equals(selectionCriteria.getCriteria())
                        && !selectionCriteria.getSelectedValue().equals(document.getValue())) {
                    return false;
                }
            }
        }
        for (Document document : docHolder.getResults()) {
            if (document.getName().equals(GlobalConstants.DATA_RANGE)
                    && Objects.nonNull(cbrRanges) && !cbrRanges.isEmpty()
                    && !isNa(document.getValue()) && !cbrRanges.contains(document.getValue())) {
                return false;
            }
            if (((document.getName().equals(GlobalConstants.DATA_CATALOG)))
                    && Objects.nonNull(cbrReferences) && !cbrReferences.isEmpty()
                    && !isNa(document.getValue())) {
                String[] catalogArray = document.getValue().split(";");
                List<String> refs = new ArrayList<>();
                for (String cat : catalogArray) {
                    refs.add(cat.trim());
                }
                if (checkAllReferences
                        && (refs.size() != cbrReferences.size() || !containsAllElements(refs, cbrReferences))) {
                    return false;
                } else if (refs.size() > 1 && !containsAllElements(refs, cbrReferences)) {
                    return false;
                } else if (refs.size() == 1 && !containsAllElements(cbrReferences, refs)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkDocumentMatch(DocHolder docHolder, List<SelectionCriteria> selectionCriterias) {
        if (Objects.isNull(selectionCriterias)) {
            return true;
        }
        for (SelectionCriteria selectionCriteria : selectionCriterias) {
            for (Document document : docHolder.getResults()) {
                if (Objects.nonNull(selectionCriteria.getSelectedValue())
                        && !isNa(selectionCriteria.getSelectedValue())
                        && document.getName().equals(selectionCriteria.getCriteria())
                        && !selectionCriteria.getSelectedValue().equals(document.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkDocumentMatch(DocHolder docHolder, Map<String, String> lightSelection,
            String selectedParameter) {
        for (Document document : docHolder.getResults()) {
            if (document.getName().equalsIgnoreCase(selectedParameter)
                    && !document.getValue().equals(lightSelection.get(selectedParameter))) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsValue(Set<AvailableValue> availableValues, String value) {
        for (AvailableValue availableValue : availableValues) {
            if (availableValue.getValue().equalsIgnoreCase(value) && availableValue.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public static int countAvailableValues(Set<AvailableValue> availableValues) {
        int sum = 0;
        for (AvailableValue availableValue : availableValues) {
            if (availableValue.isAvailable()) {
                sum++;
            }
        }
        return sum;
    }

    public static int countActiveValues(Set<AvailableValue> availableValues) {
        int sum = 0;
        for (AvailableValue availableValue : availableValues) {
            if (availableValue.isActive()) {
                sum++;
            }
        }
        return sum;
    }

    public static AvailableValue getFirstAvailableValue(Set<AvailableValue> availableValues) {
        for (AvailableValue availableValue : availableValues) {
            if (availableValue.isAvailable()) {
                return availableValue;
            }
        }
        return null;
    }

    public static AvailableValue getFirstActiveValue(Set<AvailableValue> availableValues) {
        for (AvailableValue availableValue : availableValues) {
            if (availableValue.isActive()) {
                return availableValue;
            }
        }
        return null;
    }

    public static boolean containsAvailableValue(Set<AvailableValue> availableValues) {
        for (AvailableValue availableValue : availableValues) {
            if (availableValue.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public static List<CrossFilter> getCrossFilters(List<CrossFilter> crossFilters, String productType) {
        return crossFilters.stream().filter(crossFilter -> crossFilter.getProductType().equalsIgnoreCase(productType))
                .collect(Collectors.toList());
    }

    public static List<CrossFilter> getCrossFilter(List<Map<String, String>> crossFilter) {
        List<CrossFilter> result = new ArrayList<>();
        for (Map<String, String> filter : crossFilter) {
            result.add(new CrossFilter(filter.get(GlobalConstants.DATA_PRODUCT_TYPE),
                    filter.get(GlobalConstants.DATA_SOURCE), filter.get(GlobalConstants.DATA_SOURCE_COLUMN),
                    filter.get(GlobalConstants.DATA_COLUMN_TO_FILTER), new ArrayList<>()));
        }
        return result;
    }

    public static void resetTopSelection(ConfigurationResponse configurationResponse) {
        for (Map.Entry<String, SelectionCriterias> selectionCriterias : configurationResponse.getSelectionCriterias()
                .entrySet()) {
            if (selectionCriterias.getKey().equals(GlobalConstants.DATA_TOP)) {
                for (SelectionCriteria selectionCriteria : selectionCriterias.getValue().getSelectionCriteria()) {
                    selectionCriteria.setSelectedValue(GlobalConstants.N_A);
                }
            }
        }
    }

    public static List<ConfigurationElement> getPossibleLayersToRemove(
            List<ConfigurationElement> configurationElements) {
        List<ConfigurationElement> possibleConfigurationElementsToRemove = new ArrayList<>();
        for (ConfigurationElement configurationElement : configurationElements) {
            if (configurationElement.getProductType().equals(GlobalConstants.DATA_LENS)
                    || configurationElement.getProductType().equals(GlobalConstants.DATA_TOP)
                            && Objects.nonNull(configurationElement.getCatalog())
                            && !Utils.isNa(configurationElement.getCatalog())
                            && (Objects.isNull(configurationElement.getIsDefault())
                                    || !configurationElement.getIsDefault())) {
                possibleConfigurationElementsToRemove.add(configurationElement);

            }
        }
        return possibleConfigurationElementsToRemove;
    }

    public static String getProductType(String ref, Map<String, List<String>> productTypesMap) {
        for (Map.Entry<String, List<String>> entry : productTypesMap.entrySet()) {
            if (entry.getValue().contains(ref)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String getNumberOfLayers(List<ConfigurationElement> configurationElements) {
        int numberOfLayers = 0;
        for (ConfigurationElement configurationElement : configurationElements) {
            if (configurationElement.getProductType().equals(GlobalConstants.DATA_LENS)
                    || configurationElement.getProductType().equals(GlobalConstants.DATA_TOP)
                            && Objects.nonNull(configurationElement.getCatalog())
                            && !Utils.isNa(configurationElement.getCatalog())
                            && (Objects.isNull(configurationElement.getIsDefault())
                                    || !configurationElement.getIsDefault())) {
                numberOfLayers++;

            }
        }
        return String.valueOf(numberOfLayers);
    }

    public static Set<String> extractIdsFromConfigurationElements(List<ConfigurationElement> configurationElements) {
        Set<String> ids = new HashSet<>();
        for (ConfigurationElement configurationElement : configurationElements) {
            if (Objects.nonNull(configurationElement.getCatalog())
                    && !Utils.isNa(configurationElement.getCatalog())) {
                ids.add(configurationElement.getCatalog());
            }
        }
        return ids;
    }

    public static boolean isTopSelected(SelectionCriterias selectionCriterias) {
        if (Objects.nonNull(selectionCriterias) && Objects.nonNull(selectionCriterias.getSelectionCriteria())) {
            for (SelectionCriteria selectionCriteria : selectionCriterias.getSelectionCriteria()) {
                if (!selectionCriteria.getCriteria().contains(GlobalConstants.DATA_COLOR)
                        && Objects.nonNull(selectionCriteria.getSelectedValue())
                        && !Utils.isNa(selectionCriteria.getSelectedValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsAllElements(List<String> list1, List<String> list2) {
        for (String element : list2) {
            if (!list1.contains(element)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsAtLeastOneElement(List<String> list1, List<String> list2) {
        for (String element : list2) {
            if (list1.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTopPresent(List<ConfigurationElement> configurationElements) {
        for (ConfigurationElement configurationElement : configurationElements) {
            if (configurationElement.getProductType().equals(GlobalConstants.DATA_TOP)
                    && (Objects.isNull(configurationElement.getCatalog())
                            || Utils.isNa(configurationElement.getCatalog()))) {
                return false;
            }
        }
        return true;
    }

}
