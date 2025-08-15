package com.se.service.integration;

import com.amazonaws.util.CollectionUtils;
import com.amazonaws.util.json.Jackson;
import com.se.common.DocHolder;
import com.se.common.Document;
import com.se.common.ErrorCode;
import com.se.common.ExternalDataException;
import com.se.domain.external.Criteria;
import com.se.domain.holder.CombinatoryHolder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converter for http client queries.
 */
public class JsonConverter {

    private static final Logger LOGGER = LogManager.getLogger(JsonConverter.class);

    /**
     * Transform string response of search query to Docs.
     *
     * @param jsonString body of response.
     * @return list of compiled doc holders.
     */
    List<DocHolder> toDocs(String jsonString) {
        CombinatoryHolder combinatoryHolder = Jackson.fromJsonString(jsonString, CombinatoryHolder.class);
        if (CollectionUtils.isNullOrEmpty(combinatoryHolder.getDocs())) {
            ErrorCode errorCode = ErrorCode.EXTERNAL_DATA_INCONSTANCY;
            throw new ExternalDataException("No coordination found",
                errorCode.getHttpCode(),
                errorCode.getKey(),
                errorCode.getDescription());
        }
        return combinatoryHolder.getDocs();
    }

    /**
     * Extract criteria from string response of search query.
     *
     * @param jsonString body of response.
     * @param docName    doc name to extract from response.
     * @return docs values matching docName
     */
    List<String> extractDocsValues(String jsonString, String docName) {
        LOGGER.debug("Attempt to extract criteria from string response of search query");
        return this.toDocs(jsonString).stream()
            .flatMap(docHolder -> docHolder.getResults().stream())
            .filter(document -> docName.equals(document.getName()))
            .map(Document::getValue)
            .collect(Collectors.toList());
    }

    /**
     * Transform string response of search query to criterion.
     *
     * @param jsonString body of response.
     * @return list of compiled criterion.
     */
    List<Criteria> toCriterion(String jsonString) {
        LOGGER.debug("Attempt to transform string response of search query to criterion");
        CombinatoryHolder combinatoryHolder = Jackson.fromJsonString(jsonString, CombinatoryHolder.class);
        if (CollectionUtils.isNullOrEmpty(combinatoryHolder.getCriteriaList())) {
            ErrorCode errorCode = ErrorCode.EXTERNAL_DATA_INCONSTANCY;
            throw new ExternalDataException("No criteria found",
                errorCode.getHttpCode(),
                errorCode.getKey(),
                errorCode.getDescription());
        }
        return combinatoryHolder.getCriteriaList();
    }

    /**
     * Transform string response of search query to criterion map.
     *
     * @param jsonString body of response.
     * @return map of compiled criterion.
     */
    Map<String, List<String>> toCriteriaMap(String jsonString) {
        return this.toCriterion(jsonString).stream()
            .collect(Collectors.toMap(Criteria::getId, Criteria::getValues));
    }

    /**
     * Extract criteria values from string response of search query.
     *
     * @param jsonString body of response.
     * @param criteriaId criteria to extract from response.
     * @return criteria values matching criteriaId
     */
    List<String> extractCriteriaValues(String jsonString, String criteriaId) {
        LOGGER.debug("Attempt to extract criteria values from string response of search query");
        return this.toCriteriaMap(jsonString).get(criteriaId);
    }
}
