package com.se.domain.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * It represents an IASCService in the data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectToCreate {
    private String rangeId;
    private String serviceKey;
}
