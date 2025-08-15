package com.se.service.module;

import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for ia services services properties.
 */
@Module
public class ServicePropertiesModule extends PropertiesModule {

    /**
     * Static storage link property qualifier.
     */
    public static final String STATIC_STORAGE_LINK_PROPERTY = "staticStorage";

    private static final String STATIC_STORAGE_LINK_PROPERTY_KEY = "staticStorageLink";

    /**
     * Email template default location qualifier.
     */
    public static final String MAIL_TEMPLATE_LOCATION = "mailTemplateLocation";

    /**
     * Email bom template default location qualifier.
     */
    public static final String MAIL_BOM_TEMPLATE_LOCATION = "mailBomTemplateLocation";

    /**
     * Localization file name qualifier.
     */
    public static final String LOCALIZATION_FILE_NAME = "localizationFileName";
    /**
     * Default localization file name qualifier.
     */
    public static final String DEFAULT_LOCALIZATION_FILE_NAME = "defaultLocalizationFileName";

    /**
     * Lambda file generation name qualifier.
     */
    public static final String LAMBDA_FILE_GENERATION_NAME = "lambdaFileGenerationFunctionName";
    /**
     * Lambda file generation url qualifier.
     */
    public static final String LAMBDA_FILE_GENERATION_URL = "lambdaFileGenerationUrl";
    /**
     * Lambda file generation region qualifier.
     */
    public static final String LAMBDA_FILE_GENERATION_REGION = "lambdaFileGenerationRegion";
    /**
     * Lambda file generation qualifier.
     */
    public static final String LAMBDA_FILE_GENERATION_COMMUNICATION = "lambdaFileGenerationCommunication";
    /**
     * SNS file generation topic arn qualifier.
     */
    public static final String SNS_FILE_GENERATION_TOPIC_ARN = "snsFileGenerationTopicArn";
    /**
     * SNS file generation url qualifier.
     */
    public static final String SNS_FILE_GENERATION_URL = "snsFileGenerationUrl";
    /**
     * SNS file generation region qualifier.
     */
    public static final String SNS_FILE_GENERATION_REGION = "snsFileGenerationRegion";
    /**
     * Session domain qualifier.
     */
    public static final String SESSION_DOMAIN = "sessionDomain";
    /**
     * Select url.
     */
    public static final String INTEGRATION_SELECT_URL_TEMPLATE = "integrationSelectUrlTemplate";
    /**
     * Select url.
     */
    public static final String EXPORT_EXCEL_URL_TEMPLATE = "exportExcelUrlTemplate";
    /**
     * Snc url.
     */
    public static final String INTEGRATION_SNC_URL = "integrationSncUrl";
    /**
     * Search url.
     */
    public static final String INTEGRATION_SEARCH_URL_TEMPLATE = "integrationSearchUrlTemplate";
    /**
     * Retry.
     */
    public static final String INTEGRATION_RETRY = "integrationRetry";
    /**
     * Software id.
     */
    public static final String SNC_SOFTWARE_ID = "sncSoftwareId";
    /**
     * Main guide.
     */
    public static final String SNC_MAIN_GUIDE = "sncMainGuide";
    /**
     * SNC url token.
     */
    public static final String SNC_URL_TOKEN = "sncUrlToken";
    /**
     * Connection timeout.
     */
    public static final String INTEGRATION_CONNECTION_TIMEOUT = "integrationConnectionTimeout";
    /**
     * Read timeout.
     */
    public static final String INTEGRATION_READ_TIMEOUT = "integrationReadTimeout";
    /**
     * Export excel template location.
     */
    public static final String EXPORT_EXCEL_TEMPLATE_LOCATION = "exportExcelTemplateLocation";
    /**
     * Global id.
     */
    public static final String SNC_GLOBAl_ID = "sncGlobalId";
    /**
     * Environment.
     */
    public static final String ENVIRONMENT = "environment";
    /**
     * S3 buket name.
     */
    public static final String S3_BUCKET_NAME = "s3BucketName";
    /**
     * Export mail default locale json location.
     */
    public static final String EXPORT_MAIL_DEFAULT_LOCALE_JSON_LOCATION = "exportMailDefaultLocaleJsonLocation";
    /**
     * Export mail localized locale json location.
     */
    public static final String EXPORT_MAIL_LOCALIZED_LOCALE_JSON_LOCATION = "exportMailLocalizedLocaleJsonLocation";
    /**
     * Export mail localized locale json location.
     */
    public static final String BATCH_DESCR = "batchDescr";
    /**
     * Start page filters location.
     */
    public static final String START_PAGE_FILTERS_LOCATION = "startPageFiltersLocation";
    /**
     * S3 bucket url.
     */
    public static final String S3_BUCKET_URL = "s3BucketUrl";
    /**
     * Data environment.
     */
    public static final String DATA_ENVIRONMENT = "dataEnvironement";
    /**
     * PIM description url.
     */
    public static final String PIM_DESCRIPTION_URL = "pimDescriptionUrl";
    /**
     * Substitution details template location.
     */
    public static final String SUBSTITUTION_DETAILS_TEMPLATE = "subtitutionDetailsTemplateLocation";

    private static final String EMAIL_TEMPLATE_LOCATION_PROPERTY_KEY = "s3.mail.template.location";
    private static final String LOCALIZATION_FILE_PROPERTY_KEY = "s3.localisation.file.location";
    private static final String DEFAULT_LOCALIZATION_FILE_PROPERTY_KEY = "s3.default.localisation.file.location";
    private static final String SESSION_DOMAIN_KEY = "session.domain";
    private static final String INTEGRATION_SNC_URL_KEY = "integration.snc.url";
    private static final String INTEGRATION_SELECT_URL_TEMPLATE_KEY = "integration.select.url.template";
    private static final String INTEGRATION_SEARCH_URL_TEMPLATE_KEY = "integration.search.url.template";
    private static final String INTEGRATION_RETRY_KEY = "integration.retry.count";
    private static final String SNC_SOFTWARE_ID_KEY = "integration.snc.software.id";
    private static final String SNC_MAIN_GUIDE_KEY = "integration.snc.guide.main";
    private static final String SNC_URL_TOKEN_KEY = "integration.url.token";
    private static final String INTEGRATION_CONNECTION_TIMEOUT_KEY = "integration.connection.timeout.ms";
    private static final String INTEGRATION_READ_TIMEOUT_KEY = "integration.read.timeout.ms";
    private static final String SNC_GLOBAl_ID_KEY = "integration.snc.global.id";
    private static final String ENVIRONMENT_KEY = "environment";
    private static final String EXPORT_EXCEL_URL_TEMPLATE_KEY = "s3.excel.report.template";
    private static final String EXPORT_EXCEL_TEMPLATE_LOCATION_KEY = "s3.excel.template.location";
    private static final String S3_BUCKET_NAME_KEY = "s3.bucket.name";
    private static final String EXPORT_MAIL_DEFAULT_LOCALE_JSON_LOCATION_KEY = "s3.mail.LocaleJson.default.location";
    private static final String EXPORT_MAIL_LOCALIZED_LOCALE_JSON_LOCATION_KEY = "s3.mail.LocaleJson.localized.location";
    private static final String BATCH_DESCR_KEY = "integration.snc.batch.descr";
    private static final String S3_BUCKET_URL_KEY = "s3.bucket.url";
    private static final String DATA_ENVIRONMENT_KEY = "data.environment";
    private static final String PIM_DESCRIPTION_URL_KEY = "pim.description.url";

    /**
     * Provides static storage link got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return static storage link as string.
     */
    @Singleton
    @Provides
    @Named(BATCH_DESCR)
    Boolean provideBatchDescrKey(Config config) {
        return Boolean.valueOf(getProperty(config, BATCH_DESCR_KEY));
    }

    /**
     * Provides static storage link got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return static storage link as string.
     */
    @Singleton
    @Provides
    @Named(EXPORT_MAIL_LOCALIZED_LOCALE_JSON_LOCATION)
    String provideExportMailLocalizedLocaleJsonLocationKey(Config config) {
        return getProperty(config, EXPORT_MAIL_LOCALIZED_LOCALE_JSON_LOCATION_KEY);
    }

    /**
     * Provides static storage link got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return static storage link as string.
     */
    @Singleton
    @Provides
    @Named(EXPORT_MAIL_DEFAULT_LOCALE_JSON_LOCATION)
    String provideExportMailDefaultLocaleJsonLocationKey(Config config) {
        return getProperty(config, EXPORT_MAIL_DEFAULT_LOCALE_JSON_LOCATION_KEY);
    }

    /**
     * Provides static storage link got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return static storage link as string.
     */
    @Singleton
    @Provides
    @Named(S3_BUCKET_NAME)
    String provideS3BucketNameKey(Config config) {
        return getProperty(config, S3_BUCKET_NAME_KEY);
    }

    /**
     * Provides static storage link got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return static storage link as string.
     */
    @Singleton
    @Provides
    @Named(EXPORT_EXCEL_TEMPLATE_LOCATION)
    String provideExcelTemplateLocationKey(Config config) {
        return getProperty(config, EXPORT_EXCEL_TEMPLATE_LOCATION_KEY);
    }

    /**
     * Provides static storage link got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return static storage link as string.
     */
    @Singleton
    @Provides
    @Named(EXPORT_EXCEL_URL_TEMPLATE)
    String provideExcelUrlTemplateKey(Config config) {
        return getProperty(config, EXPORT_EXCEL_URL_TEMPLATE_KEY);
    }

    /**
     * Provides static storage link got from properties.
     *
     * @param config the {@link Config} instance with properties.
     * @return static storage link as string.
     */
    @Singleton
    @Provides
    @Named(STATIC_STORAGE_LINK_PROPERTY)
    String provideStaticStorageLink(Config config) {
        return getProperty(config, STATIC_STORAGE_LINK_PROPERTY_KEY);
    }

    /**
     * Provides location of default email template.
     *
     * @param config config the {@link Config} instance with properties.
     * @return location of default email template.
     */
    @Singleton
    @Provides
    @Named(MAIL_TEMPLATE_LOCATION)
    String provideLocationDefaultTemplate(Config config) {
        return getProperty(config, EMAIL_TEMPLATE_LOCATION_PROPERTY_KEY);
    }

    /**
     * Provides location of localization files.
     *
     * @param config config the {@link Config} instance with properties.
     * @return location of localization files.
     */
    @Singleton
    @Provides
    @Named(LOCALIZATION_FILE_NAME)
    String provideLocalizationFile(Config config) {
        return getProperty(config, LOCALIZATION_FILE_PROPERTY_KEY);
    }

    /**
     * Provides location of default localization file.
     *
     * @param config config the {@link Config} instance with properties.
     * @return location of default localization file.
     */
    @Singleton
    @Provides
    @Named(DEFAULT_LOCALIZATION_FILE_NAME)
    String provideDefaultLocalizationFile(Config config) {
        return getProperty(config, DEFAULT_LOCALIZATION_FILE_PROPERTY_KEY);
    }

    /**
     * Provides session domain.
     *
     * @param config config the {@link Config} instance with properties.
     * @return session domain.
     */
    @Singleton
    @Provides
    @Named(SESSION_DOMAIN)
    String provideSessionDomain(Config config) {
        return getProperty(config, SESSION_DOMAIN_KEY);
    }

    /**
     * Provides select url.
     *
     * @param config config the {@link Config} instance with properties.
     * @return select url.
     */
    @Singleton
    @Provides
    @Named(INTEGRATION_SELECT_URL_TEMPLATE)
    String provideIntegrationSelectUrlTemplate(Config config) {
        return getProperty(config, INTEGRATION_SELECT_URL_TEMPLATE_KEY);
    }

    /**
     * Provides snc url.
     *
     * @param config config the {@link Config} instance with properties.
     * @return snc url.
     */
    @Singleton
    @Provides
    @Named(INTEGRATION_SNC_URL)
    String provideIntegrationSncUrl(Config config) {
        return getProperty(config, INTEGRATION_SNC_URL_KEY);
    }

    /**
     * Provides search url.
     *
     * @param config config the {@link Config} instance with properties.
     * @return search url.
     */
    @Singleton
    @Provides
    @Named(INTEGRATION_SEARCH_URL_TEMPLATE)
    String provideIntegrationSearchUrlTemplate(Config config) {
        return getProperty(config, INTEGRATION_SEARCH_URL_TEMPLATE_KEY);
    }

    /**
     * Provides retry.
     *
     * @param config config the {@link Config} instance with properties.
     * @return retry.
     */
    @Singleton
    @Provides
    @Named(INTEGRATION_RETRY)
    Integer provideIntegrationRetry(Config config) {
        return Integer.valueOf(getProperty(config, INTEGRATION_RETRY_KEY));
    }

    /**
     * Provides snc software id.
     *
     * @param config config the {@link Config} instance with properties.
     * @return snc software id.
     */
    @Singleton
    @Provides
    @Named(SNC_SOFTWARE_ID)
    String provideSncSoftwareId(Config config) {
        return getProperty(config, SNC_SOFTWARE_ID_KEY);
    }

    /**
     * Provides snc main guide.
     *
     * @param config config the {@link Config} instance with properties.
     * @return snc main guide.
     */
    @Singleton
    @Provides
    @Named(SNC_MAIN_GUIDE)
    String provideSncMainGuide(Config config) {
        return getProperty(config, SNC_MAIN_GUIDE_KEY);
    }

    /**
     * Provides snc url token.
     *
     * @param config config the {@link Config} instance with properties.
     * @return snc url token.
     */
    @Singleton
    @Provides
    @Named(SNC_URL_TOKEN)
    String provideSncUrlToken(Config config) {
        return getProperty(config, SNC_URL_TOKEN_KEY);
    }

    /**
     * Provides connection timeout.
     *
     * @param config config the {@link Config} instance with properties.
     * @return connection timeout.
     */
    @Singleton
    @Provides
    @Named(INTEGRATION_CONNECTION_TIMEOUT)
    Integer provideIntegrationConnectionTimeout(Config config) {
        return Integer.valueOf(getProperty(config, INTEGRATION_CONNECTION_TIMEOUT_KEY));
    }

    /**
     * Provides read timeout.
     *
     * @param config config the {@link Config} instance with properties.
     * @return read timeout.
     */
    @Singleton
    @Provides
    @Named(INTEGRATION_READ_TIMEOUT)
    Integer provideIntegrationReadTimeout(Config config) {
        return Integer.valueOf(getProperty(config, INTEGRATION_READ_TIMEOUT_KEY));
    }

    /**
     * Provides snc global id.
     *
     * @param config config the {@link Config} instance with properties.
     * @return snc global id.
     */
    @Singleton
    @Provides
    @Named(SNC_GLOBAl_ID)
    String provideSncGlobalId(Config config) {
        return getProperty(config, SNC_GLOBAl_ID_KEY);
    }

    /**
     * Provides environment.
     *
     * @param config config the {@link Config} instance with properties.
     * @return environment.
     */
    @Singleton
    @Provides
    @Named(ENVIRONMENT)
    String provideEnvironment(Config config) {
        return getProperty(config, ENVIRONMENT_KEY);
    }

    /**
     * Provides s3 bucket url.
     *
     * @param config config the {@link Config} instance with properties.
     * @return s3 bucket url.
     */
    @Singleton
    @Provides
    @Named(S3_BUCKET_URL)
    String provideS3BucketUrl(Config config) {
        return getProperty(config, S3_BUCKET_URL_KEY);
    }

    /**
     * Provides s3 bucket url.
     *
     * @param config config the {@link Config} instance with properties.
     * @return s3 bucket url.
     */
    @Singleton
    @Provides
    @Named(DATA_ENVIRONMENT)
    String provideDataEnvironment(Config config) {
        return getProperty(config, DATA_ENVIRONMENT_KEY);
    }

    /**
     * Provides pim description url.
     *
     * @param config config the {@link Config} instance with properties.
     * @return pim description url.
     */
    @Singleton
    @Provides
    @Named(PIM_DESCRIPTION_URL)
    String providePimDescriptionUrl(Config config) {
        return getProperty(config, PIM_DESCRIPTION_URL_KEY);
    }
}
