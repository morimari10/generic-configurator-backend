package com.se.handler.module;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.se.handler.action.Action;
import com.se.handler.action.ClientAndProjectAction;
import com.se.handler.action.ConfigurationDataAction;
import com.se.handler.action.CreateConfigurationAction;
import com.se.handler.action.CreateSessionAction;
import com.se.handler.action.EnergeticNeedsAction;
import com.se.handler.action.GetConfigurationAction;
import com.se.handler.action.LogErrorEventAction;
import com.se.handler.action.LogEventAction;
import com.se.handler.action.ReferencesInformationAction;
import com.se.handler.action.SaveConfigurationAction;
import com.se.handler.action.SiteAndInstallationAction;
import com.se.service.configuration.ConfigurationService;
import com.se.service.configurationStep.ConfigurationStepService;
import com.se.service.event.EventService;
import com.se.service.httpClient.HttpClient;
import com.se.service.httpClient.HttpClientConverter;
import com.se.service.httpClient.HttpClientWrapper;
import com.se.service.module.ServiceModule;
import com.se.service.session.SessionService;
import com.se.service.type.LambdaCommunicationType;
import com.se.utils.database.ApplicationDynamoDBMapper;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.joshworks.restclient.http.RestClient;

import static com.se.service.module.ServicePropertiesModule.DATA_ENVIRONMENT;
import static com.se.service.module.ServicePropertiesModule.INTEGRATION_CONNECTION_TIMEOUT;
import static com.se.service.module.ServicePropertiesModule.INTEGRATION_READ_TIMEOUT;
import static com.se.service.module.ServicePropertiesModule.INTEGRATION_RETRY;
import static com.se.service.module.ServicePropertiesModule.INTEGRATION_SEARCH_URL_TEMPLATE;
import static com.se.service.module.ServicePropertiesModule.INTEGRATION_SELECT_URL_TEMPLATE;
import static com.se.service.module.ServicePropertiesModule.INTEGRATION_SNC_URL;
import static com.se.service.module.ServicePropertiesModule.LAMBDA_FILE_GENERATION_COMMUNICATION;
import static com.se.service.module.ServicePropertiesModule.LAMBDA_FILE_GENERATION_REGION;
import static com.se.service.module.ServicePropertiesModule.LAMBDA_FILE_GENERATION_URL;
import static com.se.service.module.ServicePropertiesModule.SNC_GLOBAl_ID;
import static com.se.service.module.ServicePropertiesModule.SNC_MAIN_GUIDE;
import static com.se.service.module.ServicePropertiesModule.SNC_SOFTWARE_ID;
import static com.se.service.module.ServicePropertiesModule.SNC_URL_TOKEN;
import static com.se.service.module.ServicePropertiesModule.SNS_FILE_GENERATION_REGION;
import static com.se.service.module.ServicePropertiesModule.SNS_FILE_GENERATION_URL;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module for actions map with {@link String} key and {@link Action}
 * value.
 * Every method provides a map entry with an action implementation as value for
 * key
 * specified as the value of {@link StringKey} annotation.
 */
@Module(includes = ServiceModule.class)
@SuppressWarnings("rawtypes")
class ActionsModule {

    private static final String UNSUPPORTED_IMPL_TYPE = "Unsupported implementation type";

    /**
     * Provides a create session action.
     *
     * @param sessionService the session service.
     * @return the {@link CreateSessionAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.CREATE_SESSION)
    public Action provideCreateSessionAction(SessionService sessionService) {
        return new CreateSessionAction(sessionService);
    }

    /**
     * Provides a create configuration action value for map entry.
     *
     * @param configurationService the configuration service.
     * @return the {@link CreateConfigurationAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.CREATE_CONFIGURATION)
    public Action provideCreateConfigurationAction(ConfigurationService configurationService) {
        return new CreateConfigurationAction(configurationService);
    }

    /**
     * Provides a get configuration action value for map entry.
     *
     * @param configurationService the configuration service.
     * @return the {@link CreateConfigurationAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.GET_CONFIGURATION)
    public Action provideGetConfigurationAction(ConfigurationService configurationService) {
        return new GetConfigurationAction(configurationService);
    }

    /**
     * Provides a save configuration action value for map entry.
     *
     * @param configurationService the configuration service.
     * @return the {@link CreateConfigurationAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.SAVE_CONFIGURATION)
    public Action provideSaveConfigurationAction(ConfigurationService configurationService) {
        return new SaveConfigurationAction(configurationService);
    }

    /**
     * Provides a log event data action value for map entry.
     *
     * @param eventService the event service.
     * @return the {@link LogEventAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.LOG_EVENT_ENDPOINT)
    public Action provideLogEventAction(EventService eventService) {
        return new LogEventAction(eventService);
    }

    /**
     * Provides a get configuration data action.
     *
     * @param ConfigurationStepService the configuration service.
     * @return the {@link ConfigurationDataAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.CONFIGURATION_DATA)
    public Action provideConfigurationDataAction(ConfigurationStepService configurationStepService,
            HttpClient httpClient, HttpClientConverter httpClientConverter) {
        return new ConfigurationDataAction(configurationStepService, httpClient, httpClientConverter);
    }

    /**
     * Provides a get configuration data action.
     *
     * @param HttpClient the configuration service.
     * @return the {@link ClientAndProjectAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.CLIENT_AND_PROJECT)
    public Action provideClientAndProjectAction(HttpClient httpClient) {
        return new ClientAndProjectAction(httpClient);
    }

    /**
     * Provides a get configuration data action.
     *
     * @param HttpClient the configuration service.
     * @return the {@link SiteAndInstallationAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.SITE_AND_INSTALLATION)
    public Action provideSiteAndInstallationAction(HttpClient httpClient) {
        return new SiteAndInstallationAction(httpClient);
    }

    /**
     * Provides a get configuration data action.
     *
     * @param HttpClient the configuration service.
     * @return the {@link EnergeticNeedsAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.ENERGETIC_NEEDS)
    public Action provideEnergeticNeedsAction(HttpClient httpClient) {
        return new EnergeticNeedsAction(httpClient);
    }

    /**
     * Provides a get configuration data action.
     *
     * @param HttpClient the configuration service.
     * @return the {@link ReferencesInformationAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.REFERENCES_INFORMATION)
    public Action provideReferencesInformationAction(HttpClient httpClient) {
        return new ReferencesInformationAction(httpClient);
    }

    /**
     * Provides S&C http client wrapper.
     *
     * @param sncSelectUrlTemplate S&C select url template
     * @param retryCount           retries count
     * @param restClient           rest client
     * @param httpConverter        converter.
     * @param sncSoftware          S&C software.
     * @param sncMainGuide         S&C main guide.
     * @param sncUrlToken          S&C url token.
     * @return S &C http client
     */
    @Singleton
    @Provides
    HttpClient provideHttpClientWrapper(@Named(INTEGRATION_SELECT_URL_TEMPLATE) String sncSelectUrlTemplate,
            @Named(INTEGRATION_SEARCH_URL_TEMPLATE) String sncSearchUrlTemplate,
            @Named(INTEGRATION_RETRY) int retryCount,
            RestClient restClient, HttpClientConverter httpConverter,
            @Named(SNC_SOFTWARE_ID) String sncSoftware,
            @Named(SNC_MAIN_GUIDE) String sncMainGuide,
            @Named(SNC_URL_TOKEN) String sncUrlToken,
            @Named(INTEGRATION_SNC_URL) String sncUrl,
            @Named(DATA_ENVIRONMENT) String dataEnvironment) {
        return new HttpClientWrapper(sncSelectUrlTemplate, sncSearchUrlTemplate,
                restClient, retryCount, httpConverter,
                sncSoftware, sncMainGuide, sncUrlToken, sncUrl, dataEnvironment);
    }

    /**
     * Provides httpClientConverter.
     *
     * @param sncGlobalId S&C global id.
     * @param environment environment.
     * @return http converter
     */
    @Singleton
    @Provides
    HttpClientConverter provideHttpClientConverter(@Named(SNC_GLOBAl_ID) String sncGlobalId) {
        return new HttpClientConverter(sncGlobalId);
    }

    /**
     * Provides rest client object.
     *
     * @param connTimeout connection timeout.
     * @param readTimeout read timeout.
     * @return rest client
     */
    @Singleton
    @Provides
    RestClient provideRestClient(@Named(INTEGRATION_CONNECTION_TIMEOUT) int connTimeout,
            @Named(INTEGRATION_READ_TIMEOUT) int readTimeout) {
        return RestClient.builder().timeout(connTimeout, readTimeout).build();
    }

    /**
     * Provides a send email action value for map entry.
     *
     * @param dynamoDBMapper the mail service instance.
     * @return the {@link LogErrorEventAction} instance.
     */
    @Singleton
    @Provides
    @IntoMap
    @StringKey(ActionEndpointsConstants.LOG_ERROR)
    public Action provideLogErrorEventAction(ApplicationDynamoDBMapper dynamoDBMapper) {
        return new LogErrorEventAction(dynamoDBMapper);
    }

    /**
     * Provides aws lambda async client.
     *
     * @param lambdaCommunicationType the aws lambda communication type
     * @param lambdaUrl               the aws lambda url
     * @param lambdaRegion            the aws lambda region
     * @return created aws lambda async client.
     */
    @Singleton
    @Provides
    @Nullable
    public AWSLambdaAsync provideAWSLambdaClient(
            @Named(LAMBDA_FILE_GENERATION_COMMUNICATION) LambdaCommunicationType lambdaCommunicationType,
            @Named(LAMBDA_FILE_GENERATION_URL) String lambdaUrl,
            @Named(LAMBDA_FILE_GENERATION_REGION) String lambdaRegion) {
        switch (lambdaCommunicationType) {
            case LAMBDA_DIRECT:
                return AWSLambdaAsyncClientBuilder.standard().build();
            case LAMBDA_DIRECT_LOCAL:
                return AWSLambdaAsyncClientBuilder.standard()
                        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(lambdaUrl, lambdaRegion))
                        .build();
            case SNS:
            case SNS_LOCAL:
                return null;
            default:
                throw new IllegalArgumentException(UNSUPPORTED_IMPL_TYPE);
        }
    }

    /**
     * Provides aws sns client.
     *
     * @return aws sns client.
     */
    @Singleton
    @Provides
    @Nullable
    public AmazonSNS provideAmazonSNSClient(
            @Named(LAMBDA_FILE_GENERATION_COMMUNICATION) LambdaCommunicationType lambdaCommunicationType,
            @Named(SNS_FILE_GENERATION_URL) String snsUrl,
            @Named(SNS_FILE_GENERATION_REGION) String snsRegion) {
        switch (lambdaCommunicationType) {
            case SNS:
                return AmazonSNSClientBuilder.standard().build();
            case SNS_LOCAL:
                return AmazonSNSClientBuilder.standard()
                        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(snsUrl, snsRegion))
                        .build();
            case LAMBDA_DIRECT:
            case LAMBDA_DIRECT_LOCAL:
                return null;
            default:
                throw new IllegalArgumentException(UNSUPPORTED_IMPL_TYPE);
        }
    }

    /**
     * Provides a invoke type.
     *
     * @param lambdaCommunicationType the lambda communication type
     * @return invoke type
     */
    @Singleton
    @Provides
    @Nullable
    public InvocationType provideInvokeType(
            @Named(LAMBDA_FILE_GENERATION_COMMUNICATION) LambdaCommunicationType lambdaCommunicationType) {
        switch (lambdaCommunicationType) {
            case LAMBDA_DIRECT:
                return InvocationType.Event;
            case LAMBDA_DIRECT_LOCAL:
                return InvocationType.RequestResponse;
            case SNS:
            case SNS_LOCAL:
                return null;
            default:
                throw new IllegalArgumentException(UNSUPPORTED_IMPL_TYPE);
        }
    }
}
