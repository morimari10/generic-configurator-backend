package com.se.common.holder;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.se.common.SelectionCriterias;
import com.se.common.deserializer.SelectionCriteriasListDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * It represents an SelectionCriteriasListDeserializer parameter holder in the external system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = SelectionCriteriasListDeserializer.class)
public class SelectionCriteriasListHolder extends ConfigurationParameterHolder {
        private List<SelectionCriterias> selectionCriteriasList;
}
