package com.se.common.holder;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * It represents common params holder for config param from Configuration tab of EZ tables.
 */
@Data
public class ConfigurationParameterHolder {

    private List<Map<String, String>> parameters;
}
