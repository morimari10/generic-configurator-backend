package com.se.domain.configuration;

import java.util.Date;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity that represents configuration.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ShortConfiguration {

    private String id;
    private String configurationName;
    private Date date;
    private String locale;

    public ShortConfiguration(String id, 
        String configurationName,
        Date date, 
        String locale) {

        this.id = id;
        this.configurationName = configurationName;
        this.date = normalizeDate(date);
        this.locale = locale;
    }

/**
     * Create a MsxShortConfiguration from a MsxConfigurationEntity.
     *
     * @param entity the configuration entity
     * @return the short configuration
     */
    public static ShortConfiguration from(ConfigurationEntity entity) {
        return from(entity.getConfigurationData(), entity.getUpdatedDate());
    }

    /**
     * Create a MsxShortConfiguration from a MsxConfiguration.
     *
     * @param configuration the configuration
     * @param date          the last updated date
     * @return the short configuration
     */
    public static ShortConfiguration from(Configuration configuration, Date date) {
        return new ShortConfiguration(
            configuration.getId(),
            configuration.getName(),
            normalizeDate(date),
            configuration.getLocale()
        );
    }

    public Date getDate() {
        return normalizeDate(this.date);
    }

    public void setDate(Date date) {
        this.date = normalizeDate(date);
    }

    private static Date normalizeDate(Date date) {
        return Optional.ofNullable(date)
            .map(Date::getTime)
            .map(Date::new)
            .orElse(null);
    }

    @Override
    public String toString() {
        return id;
    }

    public ShortConfiguration(String id) {
        this.id = id;
    }
}
