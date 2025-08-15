package com.se.utils.model.export;

import lombok.Builder;
import lombok.Data;


/**
 * It represents a coordination for report.
 */
@Data
@Builder
public class ReportDevice {
    private String deviceType;
    private String groupId;
    private String description;
    private String quantity;

    public ReportDevice() {
    }

    public ReportDevice(String deviceType, String groupId, String description, String quantity) {
        this.deviceType = deviceType;
        this.groupId = groupId;
        this.description = description;
        this.quantity = quantity;
    }


}