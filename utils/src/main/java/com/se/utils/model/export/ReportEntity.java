package com.se.utils.model.export;

import lombok.Data;

import java.util.List;

/**
 * It represent an configuration for report.
 */
@Data
public class ReportEntity {
    private String configurationName;
    private String confId;
    private String reportDate;
    private List<ReportDevice> reportDevices;
}
