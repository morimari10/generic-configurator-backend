package com.se.service.httpClient;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.se.common.*;
import com.se.common.holder.SelectionGuideStructureHolder;
import com.se.domain.exception.ExternalSystemException;
import com.se.service.configurationStep.Utils;
import com.se.utils.HttpClientConstants;
import com.se.utils.SEServiceConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.RestClient;
import io.joshworks.restclient.http.exceptions.RestClientException;
import io.joshworks.restclient.request.GetRequest;
import io.joshworks.restclient.request.HttpRequest;

import org.se.selectionguide.S3RetrieveDataService;
import org.se.selectionguide.model.CombinatoryLibraryResult;

import com.amazonaws.util.json.Jackson;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class HttpClientWrapper implements HttpClient {
    private static final int CACHE_LIVE_TIME = 5;

    @SuppressWarnings("rawtypes")
    private final Cache<String, HttpResponse> wrapperCacheResponses = CacheBuilder.newBuilder()
            .expireAfterWrite(CACHE_LIVE_TIME, TimeUnit.HOURS)
            .build();

    private static final Logger LOGGER = LogManager.getLogger(HttpClientWrapper.class);
    private static final String GLOBAL_COUNTRY_CODE = "WW";
    private static final int PAGINATION = 100;
    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "Authorization";
    private static final String TIMED_OUT = "connect timed out";
    private static final String SNC_BASE_URL = "sncBaseUrl";
    private static final String GUIDE_ID = "GENERIC-CONFIGURATOR ";

    private static final String PRODUCT_DESCR_TEMPLATE = "${sncBaseUrl}/products/${id}/description/locale/${locale}";

    private final String sncSelectUrlTemplate;
    private final String sncSearchUrlTemplate;
    private final RestClient restClient;
    private final int retryCount;
    private final HttpClientConverter httpConverter;
    private final String sncSoftware;
    private final String sncMainGuide;
    private final String sncUrlToken;
    private final String sncUrl;
    private final String dataEnvironment;

    private final Map<String, String> countryMapping;
    private final Map<String, String> languageMapping;

    private final Cache<String, String> wrapperCache = CacheBuilder.newBuilder()
            .expireAfterWrite(GlobalConstants.CACHE_LIVE_TIME, TimeUnit.HOURS)
            .build();

    /**
     * Constructor.
     *
     * @param sncSelectUrlTemplate S&C select url template
     * @param sncSearchUrlTemplate S&C search url template
     * @param restClient           rest client
     * @param retryCount           retry count
     * @param httpConverter        converter
     * @param sncSoftware          S&C software
     * @param sncMainGuide         S&C main guide
     * @param sncUrlToken          S&C url token
     */
    @Inject
    public HttpClientWrapper(String sncSelectUrlTemplate, String sncSearchUrlTemplate, RestClient restClient,
            int retryCount,
            HttpClientConverter httpConverter, String sncSoftware, String sncMainGuide,
            String sncUrlToken, String sncUrl, String dataEnvironment) {

        this.sncSelectUrlTemplate = sncSelectUrlTemplate;
        this.sncSearchUrlTemplate = sncSearchUrlTemplate;
        this.restClient = restClient;
        this.retryCount = retryCount;
        this.httpConverter = httpConverter;
        this.sncSoftware = sncSoftware;
        this.sncMainGuide = sncMainGuide;
        this.sncUrlToken = sncUrlToken;
        this.sncUrl = sncUrl;
        this.dataEnvironment = dataEnvironment;

        countryMapping = ImmutableMap.<String, String>builder()
                .put("DZ", "DZA").put("AR", "ARG").put("AU", "AUS").put("AT", "AUT").put("BE", "BEL").put("BR", "BRA")
                .put("BG", "BGR").put("CA", "CAN").put("CL", "CHL").put("CN", "CHN").put("CO", "COL").put("CR", "CRI")
                .put("HR", "HRV").put("CZ", "CZE").put("DK", "DNK").put("EG", "EGY").put("AE", "ARE").put("EE", "EST")
                .put("FI", "FIN").put("FR", "FRA").put("DE", "DEU").put("GR", "GCR").put("HK", "HKG").put("HU", "HUN")
                .put("IN", "IND").put("ID", "IDN").put("IE", "IRL").put("IL", "ISR").put("IT", "ITA").put("JP", "JPN")
                .put("KZ", "KAZ").put("KR", "KOR").put("LV", "LVA").put("LT", "LTU").put("MY", "MYS").put("MX", "MEX")
                .put("MA", "MAR").put("NL", "NDL").put("NZ", "NZL").put("NG", "NGA").put("NO", "NOR").put("PE", "PER")
                .put("PH", "PHL").put("PL", "POL").put("PT", "PTR").put("RO", "ROU").put("RU", "RUS").put("SA", "SAU")
                .put("RS", "SRB").put("SG", "SGP").put("SK", "SVK").put("SI", "SVN").put("ZA", "ZAF").put("ES", "ESP")
                .put("SE", "SWE").put("CH", "CHE").put("TW", "TWN").put("TH", "THA").put("TR", "TUR").put("UA", "UKR")
                .put("UK", "GBR").put("GB", "GBR").put("US", "USA").put("VN", "VNM").put("XF", "MAR")
                .put(GLOBAL_COUNTRY_CODE, "SCHINT")
                .build();

        languageMapping = ImmutableMap.<String, String>builder()
                .put("el", "gr")
                .put("hr", "hr-hr")
                .build();
    }

    @Override
    public String getMainTableContent(String mainGuideValue, String locale) {
        LOGGER.debug("Get Main table content for the Selection -[{}]", locale);
        Instant start = Instant.now();
        String mainGuide = mainGuideValue == null ? sncMainGuide : mainGuideValue;
        String url = generateCoordinationUrl(sncSelectUrlTemplate, mainGuide, sncSoftware, locale);
        String result = performHttpRequest(restClient.get(url)).body();
        Instant finish = Instant.now();
        LOGGER.info("[GET_MAIN_TABLE(S&C)] Time execution - [{}ms]",
                Duration.between(start, finish).toMillis());
        return result;
    }

    private String generateCoordinationUrl(String urlTemplate, String globalId, String software,
            String loc) {
        String country = httpConverter.extractSnCCountry(loc);
        String lang = httpConverter.extractSnCLang(loc);
        if ("IDN".equals(country) && "IN".equals(lang)) {
            lang = "ID";
            LOGGER.info("Language_Selected - [{}]", lang);
        } else {
            LOGGER.info("Language_Selected - [{}]", lang);
        }
        Map<String, String> replaceMap = ImmutableMap.<String, String>builder()
                .put(SEServiceConstants.SOFTWARE, software)
                .put(SEServiceConstants.GLOBAL_ID, globalId)
                .put(SEServiceConstants.COUNTRY, country)
                .put(SEServiceConstants.LANGUAGE, lang)
                .build();
        return StringSubstitutor.replace(urlTemplate, replaceMap);
    }

    private HttpResponse<String> performHttpRequest(HttpRequest getRequest) {
        String url = getRequest.getUrl();
        LOGGER.debug("Attempt to perform request to the following resource - [{}]", url);
        HttpResponse<String> response = performRequest(getRequest, 0);
        if (!response.isSuccessful()) {
            LOGGER.warn("External system return the following status code - [{}], body - [{}] and status text - [{}]",
                    response.getStatus(), response.body(), response.getStatusText());
        }
        return response;
    }

    private HttpResponse<String> performRequest(HttpRequest request, int tryNumber) {
        String sncAuthorization = BEARER + " " + sncUrlToken;
        try {
            request.header(AUTHORIZATION, sncAuthorization);
            return request.asString();
        } catch (RestClientException exception) {
            String message = exception.getMessage();
            if (tryNumber < this.retryCount && StringUtils.containsIgnoreCase(message, TIMED_OUT)) {
                try {
                    LOGGER.warn("Attempt to retry request. Try number: [{}]", tryNumber + 1);
                    return performRequest(request, tryNumber + 1);
                } catch (RestClientException e) {
                    return throwError(exception);
                }
            }
            return throwError(exception);
        }
    }

    private String findGuide(String jsonString, String guideId, String loc) {
        List<Map<String, String>> selectionGuideStructure = Jackson
                .fromJsonString(jsonString, SelectionGuideStructureHolder.class).getParameters();
        Map<String, String> countryMapping = selectionGuideStructure.stream()
                .filter(m -> Objects.nonNull(m.get(guideId)))
                .collect(Collectors.toMap(m -> m.get(SEServiceConstants.COUNTRY_CLIENT), m -> m.get(guideId)));
        return httpConverter.findByCountryMapping(countryMapping, loc);
    }

    @Override
    public List<DocHolder> getSelection(List<SelectionCriteria> selectionCriterias, String mainGuideValue,
            String locale,
            String productType, Map<String, List<DocHolder>> selectionMap) {
        String globalId = findGuide(mainGuideValue, GUIDE_ID + productType, locale);
        String country = httpConverter.extractSnCCountry(locale);
        String lang = httpConverter.extractSnCLang(locale);
        Map<String, Object> criteria = new HashMap<>();

        String url = generateCoordinationUrl(sncSearchUrlTemplate, globalId, sncSoftware, locale);
        HttpRequest request = restClient.get(url)
                .queryString(HttpClientConstants.LIMIT_PARAM, PAGINATION);
        boolean shouldCheckAllParams = false;

        for (SelectionCriteria selectionCriteria : selectionCriterias) {
            if (Objects.nonNull(selectionCriteria.getSelectedValue())
                    && !Utils.isNa(selectionCriteria.getSelectedValue())) {
                request.queryString(selectionCriteria.getCriteria(), selectionCriteria.getSelectedValue());
                criteria.put(selectionCriteria.getCriteria(), selectionCriteria.getSelectedValue());
            } else if (!shouldCheckAllParams) {
                break;
            }
        }
        S3RetrieveDataService s3RetrieveDataService = new S3RetrieveDataService();
        CombinatoryLibraryResult combinatoryResult = null;
        try {
            combinatoryResult = s3RetrieveDataService.retrieveCombinatory(dataEnvironment,
                    sncSoftware,
                    globalId, country, lang, criteria, new ArrayList<>(), Integer.MAX_VALUE, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<DocHolder> docHolders = httpConverter.extractDocs(combinatoryResult.getResult());

       /*HttpResponse<String> cacheResponse = wrapperCacheResponses.getIfPresent(request.getUrl());
        HttpResponse<String> response;
        if (Objects.isNull(cacheResponse)) {
            response = performHttpRequest(request);
            wrapperCacheResponses.put(request.getUrl(), response);
        } else {
            response = cacheResponse;
        }
        List<DocHolder> docHolders = httpConverter.extractDocs(response.body());
        while (response.isSuccessful()
                && Optional.ofNullable(response.getHeaders())
                        .map(value -> value.get(HttpClientConstants.NEXT_OFFSET_HEADER))
                        .isPresent()) {
            String startOffset = response.getHeaders().getFirst(HttpClientConstants.NEXT_OFFSET_HEADER);
            LOGGER.debug("Attempt to get next page of process by the following offset - [{}]", startOffset);
            try {
                request = restClient.get(url)
                        .queryString(HttpClientConstants.LIMIT_PARAM, PAGINATION)
                        .queryString(HttpClientConstants.START_OFFSET_PARAM, startOffset);
                for (SelectionCriteria selectionCriteria : selectionCriterias) {
                    if (Objects.nonNull(selectionCriteria.getSelectedValue())
                            && !Objects.equals(selectionCriteria.getSelectedValue(), GlobalConstants.N_A)) {
                        request.queryString(selectionCriteria.getCriteria(), selectionCriteria.getSelectedValue());
                    } else {
                        break;
                    }
                }
                cacheResponse = wrapperCacheResponses.getIfPresent(request.getUrl());
                if (Objects.isNull(cacheResponse)) {
                    response = performHttpRequest(request);
                    wrapperCacheResponses.put(request.getUrl(), response);
                } else {
                    response = cacheResponse;
                }
                docHolders.addAll(httpConverter.extractDocs(response.body()));
            } catch (SERuntimeException seRuntimeException) {
                LOGGER.warn("No data found for the given startOffset-[{}]", startOffset);
            }
        }*/
        return docHolders;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DocHolder> getSelectionGuide(String mainGuideValue, String locale,
            String productType) {
        LOGGER.debug("[GET_SELECTION_GUIDE]Http client get the following product type - [{}]", productType);
        String globalId = findGuide(mainGuideValue, GUIDE_ID + productType, locale);
        String url = generateCoordinationUrl(sncSearchUrlTemplate, globalId, sncSoftware, locale);
        HttpRequest httpRequest = restClient.get(url)
                .queryString(HttpClientConstants.LIMIT_PARAM, PAGINATION);
        HttpResponse<String> cacheResponse = wrapperCacheResponses.getIfPresent(httpRequest.getUrl());
        HttpResponse<String> response;
        if (Objects.isNull(cacheResponse)) {
            response = performHttpRequest(httpRequest);
            wrapperCacheResponses.put(httpRequest.getUrl(), response);
        } else {
            response = cacheResponse;
        }
        List<DocHolder> docHolders = httpConverter.extractDocs(response.body());
        while (response.isSuccessful()
                && Optional.ofNullable(response.getHeaders())
                        .map(value -> value.get(HttpClientConstants.NEXT_OFFSET_HEADER))
                        .isPresent()) {
            String startOffset = response.getHeaders().getFirst(HttpClientConstants.NEXT_OFFSET_HEADER);
            LOGGER.debug("Attempt to get next page of process by the following offset - [{}]", startOffset);
            try {
                httpRequest = restClient.get(url)
                        .queryString(HttpClientConstants.START_OFFSET_PARAM, startOffset)
                        .queryString(HttpClientConstants.LIMIT_PARAM, PAGINATION);
                cacheResponse = wrapperCacheResponses.getIfPresent(httpRequest.getUrl());
                if (Objects.isNull(cacheResponse)) {
                    response = performHttpRequest(httpRequest);
                    wrapperCacheResponses.put(httpRequest.getUrl(), response);
                } else {
                    response = cacheResponse;
                }
                docHolders.addAll(httpConverter.extractDocs(response.body()));
            } catch (SERuntimeException seRuntimeException) {
                LOGGER.warn("No data found for the given startOffset-[{}]", startOffset);
            }
        }
        return docHolders;
    }

    private HttpResponse<String> throwError(RestClientException exception) {
        ErrorCode errorCode = ErrorCode.EXTERNAL_SYSTEM_UNAVAILABLE;
        LOGGER.error("Error occurred while calling external system. Message error: [{}], Error code: [{}]",
                exception.getMessage(), errorCode.getKey());
        throw new ExternalSystemException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                errorCode.getDescription());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DocHolder> getSelectionGuideReferences(String mainGuideValue, String locale) {
        LOGGER.debug("[GET_SELECTION_GUIDE]Http client get the following product type - [{}]", mainGuideValue);
        String url = generateCoordinationUrl(sncSearchUrlTemplate, mainGuideValue, sncSoftware, locale);
        HttpRequest httpRequest = restClient.get(url);
        HttpResponse<String> cacheResponse = wrapperCacheResponses.getIfPresent(httpRequest.getUrl());
        HttpResponse<String> response;
        if (Objects.isNull(cacheResponse)) {
            response = performHttpRequest(httpRequest);
            wrapperCacheResponses.put(httpRequest.getUrl(), response);
        } else {
            response = cacheResponse;
        }
        List<DocHolder> docHolders = httpConverter.extractDocs(response.body());
        if (Objects.equals(docHolders.size(), PAGINATION)) {
            while (response.isSuccessful()
                    && Optional.ofNullable(response.getHeaders())
                            .map(value -> value.get(HttpClientConstants.NEXT_OFFSET_HEADER))
                            .isPresent()) {
                String startOffset = response.getHeaders().getFirst(HttpClientConstants.NEXT_OFFSET_HEADER);
                LOGGER.debug("Attempt to get next page of process by the following offset - [{}]", startOffset);
                try {
                    httpRequest = restClient.get(url)
                            .queryString(HttpClientConstants.START_OFFSET_PARAM, startOffset);
                    cacheResponse = wrapperCacheResponses.getIfPresent(httpRequest.getUrl());
                    if (Objects.isNull(cacheResponse)) {
                        response = performHttpRequest(httpRequest);
                        wrapperCacheResponses.put(httpRequest.getUrl(), response);
                    } else {
                        response = cacheResponse;
                    }
                    docHolders.addAll(httpConverter.extractDocs(response.body()));
                } catch (SERuntimeException seRuntimeException) {
                    LOGGER.warn("No data found for the given startOffset-[{}]", startOffset);
                }
            }
        }
        return docHolders;
    }

    private String generateProductDescrUrl(String baseUrl, String id, String locale) {
        Map<String, String> replaceMap = ImmutableMap.<String, String>builder()
                .put(SNC_BASE_URL, baseUrl)
                .put(SEServiceConstants.ID_CLIENT, id)
                .put(SEServiceConstants.LOCALE, locale)
                .build();
        return StringSubstitutor.replace(PRODUCT_DESCR_TEMPLATE, replaceMap);
    }

    @Override
    public String getDescription(String crId, String locale) {
        LOGGER.debug("Thread {}. Getting description for CR [{}]", Thread.currentThread().getName(), crId);
        String url = generateProductDescrUrl(sncUrl, crId, locale);
        try {
            GetRequest getRequest = restClient.get(url);
            return performHttpRequest(getRequest).body();
        } catch (ExternalSystemException exception) {
            LOGGER.warn("Occurred an error during getting product with the following commercial reference - [{}]  "
                    + "description, description will be set to n/a", crId);
            return null;
        }
    }

    @Override
    public String getConfiguration(String selectionGuide, String locale) {
        Instant start = Instant.now();

        String response = wrapperCache.getIfPresent(locale + selectionGuide);

        if (Objects.isNull(response)) {
            String url = generateSnCUrl(sncUrl, sncSelectUrlTemplate, selectionGuide, locale, sncSoftware);
            HttpRequest request = restClient.get(url);
            response = performHttpRequest(request).body();
            if (!response.isEmpty()) {
                wrapperCache.put(locale + selectionGuide, response);
            }
        }

        Instant finish = Instant.now();
        LOGGER.info("[GET_ALL_CONFIGURATION(S&C)] Time execution - [{}ms]",
                Duration.between(start, finish).toMillis());
        return response;
    }

    private String generateSnCUrl(String baseUrl, String urlTemplate, String globalId, String locale,
            String software) {
        String country = extractSnCCountry(locale);
        String language = extractSnCLang(locale);
        Map<String, String> replaceMap = ImmutableMap.<String, String>builder()
                .put("sncBaseUrl", baseUrl)
                .put("software", software)
                .put("globalId", globalId)
                .put("country", country)
                .put("language", language)
                .build();
        return StringSubstitutor.replace(urlTemplate, replaceMap);
    }

    private String extractSnCCountry(String loc) {
        String country = IntegrationUtil.extractCountry(loc);
        return Optional.ofNullable(countryMapping.get(country)).orElse("SCHINT");
    }

    private String extractSnCLang(String loc) {

        // Special fix for Indonesia
        if ("id-ID".equals(loc)) {
            return "ID";
        }

        // Special fix for Israel
        if ("he-IL".equals(loc)) {
            return "EN";
        }

        Locale locale = new Locale.Builder().setLanguageTag(loc).build();
        return Optional.ofNullable(languageMapping.get(locale.getLanguage()))
                .map(s -> s.toUpperCase())
                .orElse(locale.getLanguage().toUpperCase());
    }
}
