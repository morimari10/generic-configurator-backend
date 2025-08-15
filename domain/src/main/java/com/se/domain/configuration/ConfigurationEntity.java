package com.se.domain.configuration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.domain.DBEntity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;


/**
 * Entity that represents configuration.
 */
@Data
@DynamoDBTable(tableName = "configuration")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationEntity implements DBEntity {

    /**
     * DynamoDB index name of session id column.
     */
    public static final String SESSION_ID_INDEX = "session-sessionId-idx";

    /**
     * DynamoDB index name of seamless id column.
     */
    public static final String SEAMLESS_ID_INDEX = "seamless-seamlessId-idx";

    private static final int SESSION_WIDTH_ABBREVIATE = 10;

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "createdDate")
    private Date createdDate;

    @DynamoDBAttribute(attributeName = "updatedDate")
    private Date updatedDate;

    @DynamoDBAttribute(attributeName = "sessionId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = SESSION_ID_INDEX)
    private String sessionId;

    @DynamoDBAttribute(attributeName = "seamlessId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = SEAMLESS_ID_INDEX)
    private String seamlessId;

    @DynamoDBAttribute(attributeName = "expirationDate")
    private Date expirationDate;

    // @DynamoDBAttribute set on the custom Getter.
    private Configuration configurationData;

    /**
     * Default constructor.
     */
    public ConfigurationEntity() {
        this.configurationData = new Configuration();
    }

    public Date getCreatedDate() {
        return Optional.ofNullable(createdDate).map(d -> new Date(d.getTime())).orElse(null);
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = Optional.ofNullable(createdDate).map(d -> new Date(d.getTime())).orElse(null);
    }

    public Date getUpdatedDate() {
        return Optional.ofNullable(updatedDate).map(d -> new Date(d.getTime())).orElse(null);
    }
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = Optional.ofNullable(updatedDate).map(d -> new Date(d.getTime())).orElse(null);
    }

    public Date getExpirationDate() {
        return Optional.ofNullable(expirationDate).map(d -> new Date(d.getTime())).orElse(null);
    }
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = Optional.ofNullable(expirationDate).map(d -> new Date(d.getTime())).orElse(null);
    }

    /**
     * Retrieve the configuration data properly filled-in.
     *
     * @return the configuration data
     */
    @DynamoDBAttribute(attributeName = "configurationData")
    public Configuration getConfigurationData() {
        final Configuration config = this.configurationData;
        config.setId(this.getId());
        return config;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("createdDate", createdDate)
            .append("updatedDate", updatedDate)
            .append("sessionId", StringUtils.abbreviate(sessionId, SESSION_WIDTH_ABBREVIATE))
            .append("expirationDate", expirationDate)
            .append("configurationData", configurationData)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigurationEntity that = (ConfigurationEntity) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getUpdatedDate(), that.getUpdatedDate())
            && Objects.equals(getSessionId(), that.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUpdatedDate(), getSessionId());
    }
}
