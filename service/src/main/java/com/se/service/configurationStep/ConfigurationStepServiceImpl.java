package com.se.service.configurationStep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.common.AvailableValue;
import com.se.common.ConfigurationData;
import com.se.common.ConfigurationElement;
import com.se.common.DocHolder;
import com.se.common.Document;
import com.se.common.GlobalConstants;
import com.se.common.SelectionCriteria;
import com.se.common.SelectionCriterias;
import com.se.common.Structure;
import com.se.deserializer.CrossFilterHolder;
import com.se.deserializer.SelectionCriteriaHolder;
import com.se.domain.holder.MainConfigHolder;
import com.se.service.httpClient.HttpClient;
import com.se.service.httpClient.HttpClientConverter;
import com.se.utils.model.CrossFilter;
import com.se.utils.model.configurationStep.ConfigurationResponse;

@SuppressWarnings("PMD.UselessParentheses")
public class ConfigurationStepServiceImpl implements ConfigurationStepService {
    private static final Logger LOGGER = LogManager.getLogger(ConfigurationStepServiceImpl.class);

    /**
     * Constructor.
     *
     */
    @Inject
    public ConfigurationStepServiceImpl() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public ConfigurationResponse configurationData(String body, HttpClient httpClient,
            HttpClientConverter httpClientConverter) {
        ConfigurationResponse configurationResponse = new ConfigurationResponse();
        LinkedHashMap<String, SelectionCriterias> resultSelectionCriteria = new LinkedHashMap<>();
        Map<String, Object> payload = Jackson.fromJsonString(body,
                Map.class);
        Map<String, String> selection = new HashMap<>();
        String locale = (String) payload.get(GlobalConstants.LOCALE);
        String main = (String) payload.get(GlobalConstants.MAIN);

        String mainTableContent = httpClient.getMainTableContent(main, locale);

        MainConfigHolder mainConfigHolder = Jackson.fromJsonString(mainTableContent,
                MainConfigHolder.class);

        CrossFilterHolder crossFilterHolder = null;
        List<Map<String, String>> crossFilterData = new ArrayList<>();
        try {
            crossFilterHolder = Jackson.fromJsonString(mainTableContent, CrossFilterHolder.class);
            if (crossFilterHolder != null && crossFilterHolder.getParameters() != null) {
                crossFilterData = crossFilterHolder.getParameters();
            }
        } catch (Exception e) {
            // No cross filter data, keep crossFilterData as empty list
            LOGGER.error("Error while parsing CrossFilterHolder: " + e.getMessage(), e);
        }

        List<Structure> structure = mainConfigHolder.getStructures();

        ObjectMapper mapper = new ObjectMapper();
        SelectionCriteriaHolder holder = null;
        try {
            holder = mapper.readValue(mainTableContent, SelectionCriteriaHolder.class);
        } catch (IOException e) {

            e.printStackTrace();
        }
        List<Map<String, String>> selectionCriteria = holder.getParameters();

        LOGGER.debug("Executing get configuration data for - [{}]",
                locale + "-" + selection);
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigurationData configurationData = new ConfigurationData();
        try {
            configurationData = objectMapper.convertValue(payload.get("configurationData"),
                    new TypeReference<ConfigurationData>() {
                    });
        } catch (Exception e) {
            LOGGER.debug(e);
            e.printStackTrace();
        }

        List<CrossFilter> crossFilters = Utils.getCrossFilter(crossFilterData);

        getSelection(selectionCriteria, selection,
                configurationData.getSelectionCriterias());

        // Generate all criteria selection
        List<String> allProductTypes = getAllProductTypes(selectionCriteria);

        CompletableFuture<Map<String, List<DocHolder>>> docHolderFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return Utils.getSelectionGuideParallel(allProductTypes, httpClient, main, locale);
            } catch (Exception e) {
                e.printStackTrace();
                return new HashMap<>();
            }
        });

        Map<String, List<DocHolder>> docHolderMap = new HashMap<>();

        try {
            docHolderMap = docHolderFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Map<String, List<String>> productTypesMap = new HashMap<>();
        for (String productType : allProductTypes) {
            productTypesMap.put(productType, Utils.getAllReferences(docHolderMap.get(productType)));
        }

        for (String productType : allProductTypes) {
            SelectionCriterias selectionCriterias = new SelectionCriterias();
            List<DocHolder> docHolders = docHolderMap.get(productType);

            List<Map<String, String>> productSelectionCriteria = selectionCriteria.stream()
                    .filter(criteria -> criteria.get(GlobalConstants.DATA_PRODUCT_TYPE).equalsIgnoreCase(productType))
                    .collect(Collectors.toList());

            getCriteriaSelection(productSelectionCriteria, docHolders,
                    selection, resultSelectionCriteria, selectionCriterias, productType, crossFilters,
                    configurationResponse,
                    configurationData.getConfigurationElements());

            selectionCriterias.setMainSelection(Arrays.asList(selectionCriteria.stream()
                    .filter(criteria -> criteria.get(GlobalConstants.DATA_PRODUCT_TYPE).equalsIgnoreCase(productType))
                    .findFirst().get().get(GlobalConstants.MAIN_SELECTION).split(";")));
            selectionCriterias.setGlobalRanking(selectionCriteria.stream()
                    .filter(criteria -> criteria.get(GlobalConstants.DATA_PRODUCT_TYPE).equalsIgnoreCase(productType))
                    .findFirst().get().get(GlobalConstants.DATA_GLOBAL_RANKING));
            resultSelectionCriteria.put(productType,
                    selectionCriterias);
        }
        configurationResponse.setSelectionCriterias(cleanSelectionCriterias(resultSelectionCriteria));
        configurationResponse.setStructure(structure);
        configurationResponse.setConfigurationElements(
                getConfigurationStructure(resultSelectionCriteria, locale, httpClient,
                        mainTableContent, structure,
                        docHolderMap,
                        crossFilters, selectionCriteria));

        configurationResponse.setClientAndProjects(mainConfigHolder.getClientAndProjects());

        return configurationResponse;
    }

    private LinkedHashMap<String, SelectionCriterias> cleanSelectionCriterias(
            LinkedHashMap<String, SelectionCriterias> selectionCriteriasMap) {
        LinkedHashMap<String, SelectionCriterias> cleanedSelectionCriteriasMap = new LinkedHashMap<>();

        for (Map.Entry<String, SelectionCriterias> entry : selectionCriteriasMap.entrySet()) {
            SelectionCriterias selectionCriterias = entry.getValue();

            if (Objects.isNull(selectionCriterias.getSelectionCriteria())) {
                cleanedSelectionCriteriasMap.put(entry.getKey(), selectionCriterias);
            } else {
                List<SelectionCriteria> filteredCriteria = selectionCriterias.getSelectionCriteria().stream()
                        .filter(criteria -> criteria.getAvailableValues() != null &&
                                criteria.getAvailableValues().stream().anyMatch(AvailableValue::isAvailable))
                        .collect(Collectors.toList());

                if (!filteredCriteria.isEmpty()) {
                    selectionCriterias.setSelectionCriteria(filteredCriteria);
                    cleanedSelectionCriteriasMap.put(entry.getKey(), selectionCriterias);
                }
            }
        }

        return cleanedSelectionCriteriasMap;
    }

    private Boolean applyCrossFilterValues(List<CrossFilter> crossFilters,
            List<SelectionCriteria> selectionCriteriaList,
            ConfigurationResponse configurationResponse, List<ConfigurationElement> configurationElementsInput) {
        Boolean shouldReapplyCrossFilters = false;
        for (SelectionCriteria selectionCriteria : selectionCriteriaList) {
            List<CrossFilter> crossFiltersByProductType = Utils.getCrossFilters(crossFilters,
                    selectionCriteria.getProductType());
            if (Objects.nonNull(crossFiltersByProductType) && !Objects.equals(crossFiltersByProductType.size(), 0)) {
                for (CrossFilter crossFilter : crossFiltersByProductType) {
                    if (crossFilter.getColumnToFilter().equals(selectionCriteria.getCriteria())) {
                        for (AvailableValue availableValue : selectionCriteria.getAvailableValues()) {
                            if (crossFilter.getValues().contains(availableValue.getValue())
                                    && availableValue.isAvailable()) {
                                availableValue.setAvailable(false);
                                availableValue.setActive(false);
                            }
                        }
                        if (crossFilter.getValues().contains(selectionCriteria.getSelectedValue())
                                && !Utils.isNa(selectionCriteria.getSelectedValue())) {
                            configurationResponse.setWarningMessage(true);
                            configurationResponse.setWarningMessageText("incompatible references");

                            if (Objects.nonNull(configurationElementsInput)) {
                                for (ConfigurationElement configurationElement : configurationElementsInput) {
                                    if (!crossFilter.getProductType().equalsIgnoreCase(GlobalConstants.DATA_LENS)
                                            && configurationElement.getProductType()
                                                    .equalsIgnoreCase(crossFilter.getProductType())) {
                                        if (Objects
                                                .isNull(configurationResponse
                                                        .getPossibleConfigurationElementsToRemove())) {
                                            List<ConfigurationElement> configurationElementsToAdd = new ArrayList<>();
                                            configurationElementsToAdd.add(configurationElement);
                                            configurationResponse
                                                    .setPossibleConfigurationElementsToRemove(
                                                            configurationElementsToAdd);
                                        } else {
                                            configurationResponse.getPossibleConfigurationElementsToRemove()
                                                    .add(configurationElement);
                                        }
                                    }
                                }
                            }
                            configurationResponse.setIncompatibleReferenceProductType(crossFilter.getSource());
                            selectionCriteria.setSelectedValue(GlobalConstants.N_A);
                            shouldReapplyCrossFilters = true;
                        }
                    }
                }
            }
        }
        return shouldReapplyCrossFilters;
    }

    private void setCrossFilterValues(String productType, List<CrossFilter> crossFilters, List<DocHolder> docHolders,
            List<SelectionCriteria> selectionCriteriaList) {
        Map<String, List<String>> filtering = new HashMap<>();
        for (CrossFilter crossFilter : crossFilters) {
            if (crossFilter.getSource().equalsIgnoreCase(productType)) {
                Set<String> differentValues = new HashSet<>();
                if (Objects.nonNull(filtering.get(productType + "_" + crossFilter.getSourceColumn()))) {
                    List<String> trimmedValues = filtering.get(productType + "_" + crossFilter.getSourceColumn())
                            .stream()
                            .map(String::trim)
                            .collect(Collectors.toList());
                    crossFilter.setValues(trimmedValues);
                } else {
                    for (DocHolder docHolder : docHolders) {
                        if (Utils.checkDocumentMatch(docHolder, selectionCriteriaList)) {
                            for (Document document : docHolder.getResults()) {
                                if (document.getName().equals(crossFilter.getSourceColumn())) {
                                    differentValues.add(document.getValue().trim());
                                }
                            }
                        }
                    }
                    if (differentValues.size() == 1) {
                        List<String> values = Arrays
                                .asList(((new ArrayList<>(differentValues))
                                        .get(0))
                                        .split("\\|\\|"))
                                .stream()
                                .map(String::trim)
                                .collect(Collectors.toList());
                        filtering.put(productType + "_" + crossFilter.getSourceColumn(),
                                values);
                        crossFilter.setValues(values);
                    }
                }
            }
        }
    }

    private List<ConfigurationElement> getConfigurationElements(Map<String, SelectionCriterias> resultSelectionCriteria,
            HttpClient httpClient, String mainTableContent, String locale,
            List<Structure> structures,
            Map<String, List<DocHolder>> selectionMap,
            List<CrossFilter> crossFilters,
            List<Map<String, String>> selectionCriteria) {
        List<ConfigurationElement> configurationElements = new ArrayList<>();

        int index = 0;

        for (Map.Entry<String, SelectionCriterias> selectionCriterias : resultSelectionCriteria.entrySet()) {
            String productType = selectionCriterias.getKey();

            LOGGER.debug("Executing get configuration element for - [{}]",
                    productType);
            List<DocHolder> docHolders = httpClient.getSelection(
                    Objects.isNull(selectionCriterias.getValue().getSelectionCriteria())
                            || productType.equals(GlobalConstants.DATA_LENS) ? new ArrayList<>()
                                    : selectionCriterias.getValue().getSelectionCriteria(),
                    mainTableContent, locale,
                    productType, selectionMap);
            if (Objects.nonNull(crossFilters) && !crossFilters.isEmpty()) {
                List<CrossFilter> crossFiltersByProductType = Utils.getCrossFilters(crossFilters,
                        productType);
                List<Map<String, String>> criterias = selectionCriteria.stream()
                        .filter(criteria -> criteria.get(GlobalConstants.DATA_PRODUCT_TYPE)
                                .equalsIgnoreCase(productType))
                        .collect(Collectors.toList());
                for (CrossFilter crossFilter : crossFiltersByProductType) {
                    boolean found = false;
                    if (Objects.nonNull(crossFiltersByProductType)
                            && !Objects.equals(crossFiltersByProductType.size(), 0)) {
                        for (Map<String, String> criteria : criterias) {
                            if (crossFilter.getColumnToFilter().equals(criteria.get(GlobalConstants.DATA_CRITERIA))) {
                                found = true;
                                break;
                            }
                        }
                        if (!found && crossFilter.getValues() != null
                                && !crossFilter.getValues().isEmpty()) {
                            docHolders = applyCrossFilterValuesOnDocHolders(docHolders, crossFilter);
                        }
                    }
                }
            }
            List<Structure> productTypeStructure = getStructureByProductType(mainTableContent, productType);

            productTypeStructure.sort(Comparator.comparing(struc -> struc.getZone()));
            for (Structure element : productTypeStructure) {
                Set<String> allResultsForOneParameter = getAllResultsForOneParameter(docHolders,
                        element.getProductType());
                if (Objects.equals(allResultsForOneParameter.size(), 1)) {
                    String catalog = new ArrayList<>(allResultsForOneParameter).get(0);
                    ConfigurationElement configurationElement = new ConfigurationElement(
                            String.valueOf(index),
                            element.getZone(), structures.stream()
                                    .filter(struc -> struc.getProductType()
                                            .equalsIgnoreCase(productType))
                                    .findFirst().get().getZone(),
                            productType,
                            catalog,
                            element.getProductType());
                    index++;
                    configurationElements.add(configurationElement);

                }
            }
        }
        return configurationElements;
    }

    private List<Structure> getStructureByProductType(String mainTableContent,
            String productType) {
        String className = "com.se.deserializer." + productType + "StructureHolder";
        List<Structure> structureResult = new ArrayList<>();
        try {
            Class<?> clazz = Class.forName(className);
            ObjectMapper objectMapper = new ObjectMapper();
            Object result = objectMapper.readValue(mainTableContent, clazz);
            java.lang.reflect.Method method = clazz.getMethod("getStructures");
            Object structuresObj = method.invoke(result);
            if (structuresObj instanceof List<?>) {
                for (Object obj : (List<?>) structuresObj) {
                    if (obj instanceof Structure) {
                        structureResult.add((Structure) obj);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return structureResult;
    }

    private List<ConfigurationElement> getConfigurationStructure(
            Map<String, SelectionCriterias> resultSelectionCriteria,
            String locale, HttpClient httpClient,
            String mainTableContent, List<Structure> structure,
            Map<String, List<DocHolder>> diameterSelectionMap,
            List<CrossFilter> crossFilters,
            List<Map<String, String>> selectionCriteria) {

        List<ConfigurationElement> configurationElements = getConfigurationElements(resultSelectionCriteria, httpClient,
                mainTableContent, locale, structure,
                diameterSelectionMap, crossFilters, selectionCriteria);

        return configurationElements;
    }

    private Set<String> getAllResultsForOneParameter(List<DocHolder> docHolders, String parameter) {
        Set<String> result = new HashSet<>();
        for (DocHolder docHolder : docHolders) {
            for (Document document : docHolder.getResults()) {
                if (document.getName().equals(parameter)) {
                    result.add(document.getValue());
                }
            }
        }
        return result;
    }

    private void getSelection(List<Map<String, String>> selectionCriteria, Map<String, String> selection,
            Map<String, SelectionCriterias> selectionCriterias) {
        if (Objects.nonNull(selectionCriterias)) {
            for (Map<String, String> criteria : selectionCriteria) {
                if (Objects.nonNull(criteria.get(GlobalConstants.DATA_CRITERIA))
                        && Objects.nonNull(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE))
                        && Objects.nonNull(selectionCriterias.get(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE)))
                        && Objects.nonNull(selectionCriterias.get(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE))
                                .getSelectionCriteria())) {
                    for (SelectionCriteria selectionCriteriaLoop : selectionCriterias
                            .get(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE))
                            .getSelectionCriteria()) {
                        if (selectionCriteriaLoop.getCriteria()
                                .equalsIgnoreCase(criteria.get(GlobalConstants.DATA_CRITERIA))
                                && Objects.nonNull(selectionCriteriaLoop.getSelectedValue())
                                && !Utils.isNa(selectionCriteriaLoop.getSelectedValue())) {
                            selection.put(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE) + "_"
                                    + criteria.get(GlobalConstants.DATA_CRITERIA),
                                    selectionCriteriaLoop.getSelectedValue());
                        }

                    }

                }

            }
        }

    }

    private List<String> getAllProductTypes(List<Map<String, String>> selectionCriteria) {
        LinkedHashSet<String> allProductTypes = new LinkedHashSet<>();
        for (Map<String, String> criteria : selectionCriteria) {
            String productType = criteria.get(GlobalConstants.DATA_PRODUCT_TYPE);
            if (Objects.nonNull(productType)) {
                allProductTypes.add(productType);
            }
        }
        List<String> sortedProductTypes = new ArrayList<>(allProductTypes);

        sortedProductTypes.sort(Comparator.comparing(productType -> {
            return selectionCriteria.stream()
                    .filter(criteria -> productType.equals(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE)))
                    .map(criteria -> criteria.get(GlobalConstants.DATA_GLOBAL_RANKING))
                    .findFirst()
                    .orElse("");
        }));
        return sortedProductTypes;
    }

    private void computeCriteriaSelection(List<Map<String, String>> criterias,
            List<DocHolder> docHolders, Map<String, String> selection,
            Map<String, SelectionCriterias> resultSelectionCriteria,
            List<SelectionCriteria> selectionCriteriasList) {

        criterias.sort(Comparator.comparing(criteria -> criteria.get(GlobalConstants.DATA_RANKING)));
        boolean lastSelectedValueSet = true;

        Integer index = 0;
        for (Map<String, String> criteria : criterias) {
            Set<AvailableValue> availableValues = new HashSet<>();
            Set<String> unavailableValues = new HashSet<>();
            for (DocHolder docHolder : docHolders) {
                if (Utils.checkDocumentMatch(docHolder, selectionCriteriasList)) {
                    for (Document document : docHolder.getResults()) {
                        if (document.getName().equals(criteria.get(GlobalConstants.DATA_CRITERIA))
                                && !Utils.isNa(document.getValue())) {
                            availableValues.add(new AvailableValue(document.getValue(), false, true));
                        }
                    }
                } else {
                    for (Document document : docHolder.getResults()) {
                        if (document.getName().equals(criteria.get(GlobalConstants.DATA_CRITERIA))
                                && !Utils.isNa(document.getValue())) {
                            unavailableValues.add(document.getValue());
                        }
                    }
                }
            }
            if (!Objects.equals(criteria.get(GlobalConstants.DATA_UI_COMPONENT), GlobalConstants.DATA_TOGGLE)) {
                for (String unavailableValue : unavailableValues) {
                    if (!Utils.containsValue(availableValues, unavailableValue)) {
                        availableValues.add(new AvailableValue(unavailableValue, false, false));
                    }
                }
            }
            if (Objects.nonNull(availableValues) && availableValues.size() != 0
                    && Utils.containsAvailableValue(availableValues)) {
                SelectionCriteria selectionCriteria = new SelectionCriteria(
                        criteria.get(GlobalConstants.DATA_PRODUCT_TYPE),
                        criteria.get(GlobalConstants.DATA_CRITERIA),
                        criteria.get(GlobalConstants.DATA_LABEL),
                        criteria.get(GlobalConstants.DATA_RANKING),
                        criteria.get(GlobalConstants.DATA_UI_COMPONENT));
                selectionCriteria = setDefaultValue(availableValues, criteria, selectionCriteria,
                        resultSelectionCriteria);
                lastSelectedValueSet = setSelectedValue(availableValues, criteria, selection,
                        selectionCriteriasList,
                        selectionCriteria, lastSelectedValueSet, index);
                List<AvailableValue> availableValuesSorted = new ArrayList<>();
                if (Objects.nonNull(selectionCriteria.getDefaultValue())
                        && !selectionCriteria.getDefaultValue().isEmpty()
                        && !Utils.isNa(selectionCriteria.getDefaultValue())) {
                    String defaultValue = selectionCriteria.getDefaultValue();
                    AvailableValue defaultValueObject = availableValues.stream()
                            .filter(value -> value.getValue().equals(defaultValue)).findFirst()
                            .get();
                    if (Objects.nonNull(defaultValueObject)) {
                        availableValuesSorted.add(defaultValueObject);
                        List<AvailableValue> availableValuesList = new ArrayList<>(availableValues);
                        availableValuesList.remove(defaultValueObject);
                        availableValuesList.sort(Comparator.comparing(AvailableValue::getValue));
                        availableValuesSorted.addAll(availableValuesList);
                    } else {
                        availableValuesSorted.addAll(availableValues);
                        availableValuesSorted.sort(Comparator.comparing(AvailableValue::getValue));
                    }
                } else {
                    availableValuesSorted.addAll(availableValues);
                    availableValuesSorted.sort(Comparator.comparing(AvailableValue::getValue));
                }
                selectionCriteria.setAvailableValues(availableValuesSorted);

                if (Objects.nonNull(criteria.get(GlobalConstants.INFO_TIP))
                        && !Utils.isNa(criteria.get(GlobalConstants.INFO_TIP))) {
                    selectionCriteria.setInfoTip(criteria.get(GlobalConstants.INFO_TIP));
                }
                if (Objects.nonNull(criteria.get(GlobalConstants.INFO_TIP_PER_AVAILABLE_VALUE))
                        && !Utils.isNa(criteria.get(GlobalConstants.INFO_TIP_PER_AVAILABLE_VALUE))) {
                    String infoTipValue = criteria.get(GlobalConstants.INFO_TIP_PER_AVAILABLE_VALUE);
                    boolean infoTipBoolean = infoTipValue.equalsIgnoreCase("yes");
                    selectionCriteria.setInfoTipPerAvailableValue(infoTipBoolean);
                }
                selectionCriteriasList.add(selectionCriteria);

            }
            index++;
        }
    }

    private List<DocHolder> applyCrossFilterValuesOnDocHolders(List<DocHolder> docHolders,
            CrossFilter crossFilter) {
        List<DocHolder> filteredDocHolders = new ArrayList<>();
        if (crossFilter.getValues().size() == 1 && Utils.isNa(crossFilter.getValues().get(0))) {
            return docHolders;
        }
        for (DocHolder docHolder : docHolders) {
            boolean matches = true;
            for (Document document : docHolder.getResults()) {
                if (document.getName().equals(crossFilter.getColumnToFilter())
                        && crossFilter.getValues().contains(document.getValue())) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                filteredDocHolders.add(docHolder);
            }
        }
        return filteredDocHolders;
    }

    private void getCriteriaSelection(List<Map<String, String>> criterias,
            List<DocHolder> docHolders, Map<String, String> selection,
            Map<String, SelectionCriterias> resultSelectionCriteria, SelectionCriterias selectionCriterias,
            String productType, List<CrossFilter> crossFilters, ConfigurationResponse configurationResponse,
            List<ConfigurationElement> configurationElementsInput) {

        boolean shouldReapplyCrossFilters;
        List<SelectionCriteria> selectionCriteriasList = new ArrayList<>();

        setCrossFilterValues(productType, crossFilters, docHolders, selectionCriteriasList);
        List<CrossFilter> crossFiltersByProductType = Utils.getCrossFilters(crossFilters,
                productType);
        for (CrossFilter crossFilter : crossFiltersByProductType) {
            boolean found = false;
            if (Objects.nonNull(crossFiltersByProductType) && !Objects.equals(crossFiltersByProductType.size(), 0)) {
                for (Map<String, String> criteria : criterias) {
                    if (crossFilter.getColumnToFilter().equals(criteria.get(GlobalConstants.DATA_CRITERIA))) {
                        found = true;
                        break;
                    }
                }
                if (!found && crossFilter.getValues() != null
                        && !crossFilter.getValues().isEmpty()) {
                    docHolders = applyCrossFilterValuesOnDocHolders(docHolders, crossFilter);
                }
            }
        }
        computeCriteriaSelection(criterias, docHolders, selection, resultSelectionCriteria, selectionCriteriasList);

        do {
            shouldReapplyCrossFilters = false;
            setCrossFilterValues(productType, crossFilters, docHolders, selectionCriteriasList);
            shouldReapplyCrossFilters = applyCrossFilterValues(crossFilters, selectionCriteriasList,
                    configurationResponse, configurationElementsInput);

            selectionCriterias.setSelectionCriteria(selectionCriteriasList);
        } while (shouldReapplyCrossFilters);
    }

    private boolean setSelectedValue(Set<AvailableValue> availableValues, Map<String, String> criteria,
            Map<String, String> selection, List<SelectionCriteria> selectionCriterias,
            SelectionCriteria selectionCriteria, boolean lastSelectedValueSet,
            Integer criteriaIndex) {
        if ((lastSelectedValueSet)
                && selection.containsKey(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE) + "_"
                        + criteria.get(GlobalConstants.DATA_CRITERIA))
                && Utils.containsValue(availableValues,
                        selection.get(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE) + "_"
                                + criteria.get(GlobalConstants.DATA_CRITERIA)))
                && (Utils.allSelectionCriteriaSelected(selectionCriterias))) {
            selectionCriteria.setSelectedValue(selection.get(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE) + "_"
                    + criteria.get(GlobalConstants.DATA_CRITERIA)));
            setSelectedValueInAvailableValues(
                    selection.get(criteria.get(GlobalConstants.DATA_PRODUCT_TYPE) + "_"
                            + criteria.get(GlobalConstants.DATA_CRITERIA)),
                    availableValues);
            return true;
        } else if ((lastSelectedValueSet) && Objects.nonNull(selectionCriteria.getDefaultValue())
                && !Utils.isNa(selectionCriteria.getDefaultValue())) {
            selectionCriteria.setSelectedValue(selectionCriteria.getDefaultValue());
            setSelectedValueInAvailableValues(selectionCriteria.getDefaultValue(), availableValues);
            selection.put(
                    criteria.get(GlobalConstants.DATA_PRODUCT_TYPE) + "_" + criteria.get(GlobalConstants.DATA_CRITERIA),
                    selectionCriteria.getDefaultValue());
            return true;
        } else if ((lastSelectedValueSet)
                && Objects.equals(Utils.countAvailableValues(availableValues), 1)
                && (criteriaIndex != 0)) {
            selectionCriteria.setSelectedValue(Utils.getFirstAvailableValue(availableValues).getValue());
            return true;
        } else if ((lastSelectedValueSet)
                && Objects.equals(Utils.countActiveValues(availableValues), 1)
                && (criteriaIndex != 0)) {
            selectionCriteria.setSelectedValue(Utils.getFirstActiveValue(availableValues).getValue());
            return true;
        } else {
            selectionCriteria.setSelectedValue(GlobalConstants.N_A);
            selection.put(
                    criteria.get(GlobalConstants.DATA_PRODUCT_TYPE) + "_" + criteria.get(GlobalConstants.DATA_CRITERIA),
                    GlobalConstants.N_A);
            return false;
        }
    }

    private void setSelectedValueInAvailableValues(String selectedValue, Set<AvailableValue> availableValues) {
        for (AvailableValue availableValue : availableValues) {
            if (Objects.equals(availableValue.getValue(), selectedValue)) {
                availableValue.setActive(true);
                return;
            }
        }
    }

    private SelectionCriteria setDefaultValue(Set<AvailableValue> availableValues, Map<String, String> criteria,
            SelectionCriteria selectionCriteria, Map<String, SelectionCriterias> resultSelectionCriterias) {
        Boolean isToggle = criteria.get(GlobalConstants.DATA_UI_COMPONENT)
                .equalsIgnoreCase(GlobalConstants.DATA_TOGGLE);
        if ((isToggle)
                && Objects.equals(Utils.countAvailableValues(availableValues), 1)
                && !criteria.get(GlobalConstants.DATA_RANKING).equals("1")) {
            selectionCriteria.setDefaultValue(Utils.getFirstAvailableValue(availableValues).getValue());
        } else if ((isToggle)
                && Objects.nonNull(criteria.get(GlobalConstants.DATA_DEFAULT_VALUE))
                && Utils.containsValue(availableValues, criteria.get(GlobalConstants.DATA_DEFAULT_VALUE))) {
            selectionCriteria.setDefaultValue(criteria.get(GlobalConstants.DATA_DEFAULT_VALUE));
        } else if ((criteria.get(GlobalConstants.DATA_UI_COMPONENT).equalsIgnoreCase(GlobalConstants.DATA_TOGGLE))
                && Objects.nonNull(criteria.get(GlobalConstants.DATA_DEFAULT_VALUE))
                && criteria.get(GlobalConstants.DATA_DEFAULT_VALUE).contains("@")) {
            String[] parts = criteria.get(GlobalConstants.DATA_DEFAULT_VALUE).substring(1).split("_");
            String productType = parts[0];
            String criteriaToRecover = parts[1];
            if (Objects.nonNull(resultSelectionCriterias.get(productType))
                    && Objects.nonNull(resultSelectionCriterias.get(productType).getSelectionCriteria())) {
                for (SelectionCriteria selectionCriteria2 : resultSelectionCriterias.get(productType)
                        .getSelectionCriteria()) {
                    if (selectionCriteria2.getCriteria().equalsIgnoreCase(criteriaToRecover)
                            && Objects.nonNull(selectionCriteria2.getSelectedValue())
                            && !Utils.isNa(selectionCriteria2.getSelectedValue())
                            && Utils.containsValue(availableValues, selectionCriteria2.getSelectedValue())) {
                        selectionCriteria.setDefaultValue(selectionCriteria2.getSelectedValue());
                    }
                }
            }
        }
        return selectionCriteria;

    }
}
