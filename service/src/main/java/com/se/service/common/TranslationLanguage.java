package com.se.service.common;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.json.Jackson;
import com.google.common.collect.ImmutableMap;
import com.se.common.ErrorCode;
import com.se.utils.RessourceUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TranslationLanguage {
    private static final Logger LOGGER = LogManager.getLogger(TranslationLanguage.class);

    private static final String LANGUAGE_LABEL = "language";
    private static final String COUNTRY_LABEL = "country";
    private static final String LOCAL_TRANSLATION_STUB = "templates/mail-locale.json";
    private static final String EMAIL_TRANSLATION_KEY_PATTERN = "genericconfigurator.mail.";

    private AmazonS3 amazonS3Client;
    private String bucketName;
    private String localizationFileName;
    private String defaultLocalizationFileName;
    private String templateName;
    private Map<String, Object> translation;

    /**
     * Parametrized constructor.
     *
     * @param locale   the locale
     * @param amazonS3Client              the amazon s3 client
     * @param bucketName                  the s3 bucket name
     * @param localizationFileName        the mail localization template name
     * @param defaultLocalizationFileName the default mail localization file name
     * @param templateName                the default template name
     * @param dataObj                     data template
     */
    public TranslationLanguage(Locale locale, AmazonS3 amazonS3Client, String bucketName, String localizationFileName,
        String defaultLocalizationFileName, String templateName, Object dataObj) {
        this.amazonS3Client = amazonS3Client;
        this.bucketName = bucketName;
        this.localizationFileName = localizationFileName;
        this.defaultLocalizationFileName = defaultLocalizationFileName;
        this.templateName = templateName;

        try {
            translation = getTranslationFile(locale);
            LOGGER.debug("getTranslationFile Ok");
            Map<String, Object> localTranslation = safeConvertToJson(loadInternalLocalizationFile(),
                    LOCAL_TRANSLATION_STUB);
            localTranslation.forEach(translation::putIfAbsent);
        } catch (Exception e) {
            LOGGER.debug("No S3 bucket access in local, get just local label");
            this.translation = safeConvertToJson(loadInternalLocalizationFile(),
                LOCAL_TRANSLATION_STUB);
        }
        // Replace line break special character by HTML line breaks.
        for (Map.Entry<String, Object> entry : translation.entrySet()) {
            entry.setValue(((String) entry.getValue()).replace("{n}", "<br/>"));
        }
        if (null != dataObj) {
            translation.put("emailData", dataObj);
        }
    }

    
    /**
     * Parametrized constructor.
     *
     * @param locale   the locale
     * @param amazonS3Client              the amazon s3 client
     * @param bucketName                  the s3 bucket name
     * @param localizationFileName        the mail localization template name
     * @param defaultLocalizationFileName the default mail localization file name
     * @param templateName                the default template name
     * @param dataObj                     data template
     */
    public TranslationLanguage(Locale locale, AmazonS3 amazonS3Client, String bucketName, String localizationFileName,
        String defaultLocalizationFileName) {
        this.amazonS3Client = amazonS3Client;
        this.bucketName = bucketName;
        this.localizationFileName = localizationFileName;
        this.defaultLocalizationFileName = defaultLocalizationFileName;

        try {
            translation = getTranslationFile(locale);
            LOGGER.debug("getTranslationFile Ok");
            Map<String, Object> localTranslation = safeConvertToJson(loadInternalLocalizationFile(),
                    LOCAL_TRANSLATION_STUB);
            localTranslation.forEach(translation::putIfAbsent);
        } catch (Exception e) {
            LOGGER.debug("No S3 bucket access in local, get just local label");
            this.translation = safeConvertToJson(loadInternalLocalizationFile(),
                LOCAL_TRANSLATION_STUB);
        }
        // Replace line break special character by HTML line breaks.
        for (Map.Entry<String, Object> entry : translation.entrySet()) {
            entry.setValue(((String) entry.getValue()).replace("{n}", "<br/>"));
        }
    }

    /**
     * Get a translated String from a key.
     *
     * @param key Key of the String to translate.
     * @return Translated String.
     */
    public String get(String key) {
        if (translation.containsKey(key)) {
            return translation.get(key).toString();
        } else {
            return key;
        }
    }

    /**
     * Get the entire Map translation Stirng.
     *
     * @return Translated Mapping.
     */
    public Map<String, Object> getTranslationMap() {
        return translation;
    }

    private Map<String, Object> getTranslationFile(Locale locale) {
        Map<String, String> replaceMap = ImmutableMap.<String, String>builder()
                .put(LANGUAGE_LABEL, locale.getLanguage())
                .put(COUNTRY_LABEL, locale.getCountry())
                .build();
        String translationConfig = StringSubstitutor.replace(localizationFileName, replaceMap).toLowerCase();
        LOGGER.debug("Attempt to get the following translation - [{}]", translationConfig);
        try {
            return removeKeyPrefixes(safeConvertToJson(amazonS3Client.getObjectAsString(bucketName, translationConfig),
                    translationConfig));
        } catch (SdkClientException exception) {
            ErrorCode errorCode = ErrorCode.MAIL_SEND_FAIL;
            LOGGER.error("Warning locale - [{}] doesn't found, system will used default locale."
                            + " Message error: [{}], Error code: [{}]",
                    translationConfig, exception.getMessage(), errorCode.getKey());
            LOGGER.debug("Attempt to find default localization config - [{}]  [{}]  [{}] ", templateName, bucketName,
                defaultLocalizationFileName);
            return removeKeyPrefixes(safeConvertToJson(amazonS3Client.getObjectAsString(bucketName,
                    defaultLocalizationFileName), defaultLocalizationFileName));
        }
    }

    /**
     * Convert loaded data as string to JSON with processing possible duplicated keys in data.
     *
     * @param config            config as string
     * @param translationConfig loaded resource name
     * @return JSON with localization data
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> safeConvertToJson(String config, String translationConfig) {
        if (StringUtils.isEmpty(config)) {
            return Collections.emptyMap();
        }

        try {
            // read values to map first to squash duplicated keys in localization config
            // to avoid json parse exception
            return Jackson.getObjectMapper().readValue(config, Map.class);
        } catch (IOException exception) {
            ErrorCode errorCode = ErrorCode.MAIL_SEND_FAIL;
            LOGGER.error("Can't parse localization configuration for locale - [{}]"
                            + " Message error: [{}], Error code: [{}]",
                    translationConfig, exception.getMessage(), errorCode.getKey());
            return Collections.emptyMap();
        }
    }

    private Map<String, Object> removeKeyPrefixes(Map<String, Object> stringObjectMap) {
        return stringObjectMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(EMAIL_TRANSLATION_KEY_PATTERN))
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .collect(Collectors.toMap(entry -> entry.getKey().replace(EMAIL_TRANSLATION_KEY_PATTERN,
                        StringUtils.EMPTY), Map.Entry::getValue));
    }

    /**
     * Load JSON-stub with english translations for email template from local resource.
     *
     * @return config file content as string
     */
    private String loadInternalLocalizationFile() {
        try (InputStream inputStream = RessourceUtils.getResourceAsStream(getClass(), LOCAL_TRANSLATION_STUB)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            LOGGER.error("Can't load internal localization stub. Message error: [{}]", exception.getMessage());
            return null;
        }
    }
}
