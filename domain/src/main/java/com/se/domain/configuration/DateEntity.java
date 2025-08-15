package com.se.domain.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Common entity for date management.
 */
public abstract class DateEntity {
    /**
     * Creation Date.
     */
    @DynamoDBAttribute
    @JsonIgnore
    private Date createdDate;

    /**
     * Last update Date.
     */
    @DynamoDBAttribute
    @JsonIgnore
    private Date updatedDate;

    /**
     * Expiration Date.
     */
    @DynamoDBAttribute
    @JsonIgnore
    private Date expirationDate;

    public Date getCreatedDate() {
        return cloneDate(createdDate);
    }

    public void setCreatedDate(Date date) {
        this.createdDate = cloneDate(date);
    }

    public Date getUpdatedDate() {
        return cloneDate(updatedDate);
    }

    public void setUpdatedDate(Date date) {
        this.updatedDate = cloneDate(date);
    }

    public Date getExpirationDate() {
        return cloneDate(expirationDate);
    }

    public void setExpirationDate(Date date) {
        this.expirationDate = cloneDate(date);
    }

    private Date cloneDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return new Date(date.getTime());
        }
    }
}
