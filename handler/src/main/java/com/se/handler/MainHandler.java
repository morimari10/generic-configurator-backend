package com.se.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.Jackson;
import com.se.handler.module.DaggerMainComponent;
import com.se.domain.configuration.ResponseWrapper;
import com.se.domain.exception.ServiceException;
import com.se.common.SERuntimeException;
import com.se.domain.session.Session;
import com.se.handler.action.Action;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.se.common.ErrorCode.NO_SUCH_ENDPOINT;
import static com.se.common.ErrorCode.UNKNOWN_ERROR;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;

/**
 * Main lambda handler. Entry point for all requests.
 * Lambda request handlers implement AWS Lambda Function application logic using
 * plain old java objects
 * as input and output.
 */
@SuppressWarnings("rawtypes")
public class MainHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final Logger LOGGER = LogManager.getLogger(MainHandler.class);

    private static final String PATH_COMMON_PART_PATTERN = "^(/\\w+)?/generic-configurator/";
    private static final String ACTION_KEY_FORMAT = "%s=%s";
    private static final String ACAO_HEADER = "Access-Control-Allow-Origin";
    private static final String ACAC_HEADER = "Access-Control-Allow-Credentials";
    private static final String SET_COOKIE_HEADER = "Set-Cookie";
    private static final Map<String, String> HEADERS;
    private static final String OPTIONS_METHOD = "OPTIONS";
    private static final Map<String, String> OPTIONS_HEADERS;

    static {
        HEADERS = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>("Strict-Transport-Security", "max-age=15768000; includeSubDomains"),
                new AbstractMap.SimpleEntry<>("X-XSS-Protection", "1"),
                new AbstractMap.SimpleEntry<>("X-Content-Type-Options", "nosniff"))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
        OPTIONS_HEADERS = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>("Access-Control-Allow-Headers",
                        "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token"),
                new AbstractMap.SimpleEntry<>("Access-Control-Allow-Methods", "POST,OPTIONS,PUT,PATCH"))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
    }

    private Map<String, Action> actionMap;
    private String acaoValue;
    private String acacValue = "true";

    /**
     * Default constructor. Build dagger components and then inject this in
     * application context.
     */
    public MainHandler() {
        DaggerMainComponent.builder().build().inject(this);
    }

    @Inject
    void setActionMap(Map<String, Action> actionMap) {
        this.actionMap = actionMap;
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        LOGGER.debug("Start handling request.");
        Action logErrorEventAction = actionMap.get("POST=log-error");
        try {
            return startLambda(input);
        } catch (ServiceException exception) {
            logErrorEventAction.perform(input, exception.getErrorCode());
            LOGGER.error("Occurs the error with following code - [{}] and error template - [{}]",
                    exception.getErrorCode(), exception.getErrorTemplate(), exception);
            return buildErrorResponse(exception.getHttpCode(), exception.getErrorCode(),
                    input.getRequestContext().getRequestId(), context.getAwsRequestId(), exception.getValues());
        } catch (SERuntimeException exception) {
            logErrorEventAction.perform(input, exception.getErrorCode());
            LOGGER.error("Occurs the error with following code - [{}] and error template - [{}]",
                    exception.getErrorCode(), exception.getErrorTemplate(), exception);
            return buildErrorResponse(exception.getHttpCode(), exception.getErrorCode(),
                    input.getRequestContext().getRequestId(), context.getAwsRequestId());
        } catch (Exception exception) {
            logErrorEventAction.perform(input, String.valueOf(UNKNOWN_ERROR.getHttpCode()));
            LOGGER.error("Occurs an unknown error", exception);
            return buildErrorResponse(UNKNOWN_ERROR.getHttpCode(), UNKNOWN_ERROR.getKey(),
                    input.getRequestContext().getRequestId(), context.getAwsRequestId());
        }
    }

    private AwsProxyResponse startLambda(AwsProxyRequest input) {
        acaoValue = getAcaoOriginHeader(input.getHeaders());
        if (Objects.equals(input.getHttpMethod(), OPTIONS_METHOD)) {
            LOGGER.debug("Performing option");
            return mockOptionsRequest();
        } else {
            Action action = findAction(input.getPath(), input.getHttpMethod());
            LOGGER.debug("Performing action [{}] for endpoint.", action.getClass().getSimpleName());

            Object payload = action.perform(input);
            LOGGER.debug("Action [{}] performed.", action.getClass().getSimpleName());
            switch (action.getActionType()) {
                case EL_BRIDGE:
                    return buildRedirectResponse(payload);
                case SESSION:
                    return buildSessionResponse(payload);
                default:
                    return buildSuccessfulResponse(payload);
            }
        }
    }

    private String getAcaoOriginHeader(Map<String, String> headers) {
        String originHeader = Optional.ofNullable(headers.get("Origin")).orElse(headers.get("origin"));
        return originHeader;
    }

    private Action findAction(String path, String method) {
        String endpoint = path.replaceFirst(PATH_COMMON_PART_PATTERN, "");
        String actionKey = String.format(ACTION_KEY_FORMAT, method, endpoint);
        LOGGER.debug("Requested action key is [{}].", actionKey);

        Action action = actionMap.get(actionKey);
        if (Objects.isNull(action)) {
            LOGGER.error("There is no action for [{}] key.", actionKey);
            throw new ServiceException(NO_SUCH_ENDPOINT);
        }
        return action;
    }

    private AwsProxyResponse buildSuccessfulResponse(Object payload) {
        int code = HttpStatus.SC_OK;
        return buildResponse(code, payload);
    }

    private AwsProxyResponse buildErrorResponse(int code, String errorCode, String gatewayRequestId,
            String integrationRequestId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("errorCode", errorCode);
        payload.put("gatewayRequestId", gatewayRequestId);
        payload.put("integrationRequestId", integrationRequestId);
        payload.put("isApiGatewayError", Boolean.FALSE);
        return buildResponse(code, payload);
    }

    private AwsProxyResponse buildErrorResponse(int code, String errorCode, String gatewayRequestId,
            String integrationRequestId, List<String> values) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("errorCode", errorCode);
        payload.put("values", values);
        payload.put("gatewayRequestId", gatewayRequestId);
        payload.put("integrationRequestId", integrationRequestId);
        payload.put("isApiGatewayError", Boolean.FALSE);
        return buildResponse(code, payload);
    }

    private AwsProxyResponse buildResponse(int code, Object payload) {
        String body = Jackson.toJsonPrettyString(new ResponseWrapper<>(code, payload));
        AwsProxyResponse awsProxyResponse = new AwsProxyResponse(code);
        awsProxyResponse.setBody(body);
        awsProxyResponse.setHeaders(createHeadersMap(acaoValue, acacValue));
        return awsProxyResponse;
    }

    private AwsProxyResponse buildRedirectResponse(Object redirectUrl) {
        AwsProxyResponse awsProxyResponse = new AwsProxyResponse(HttpStatus.SC_MOVED_PERMANENTLY);
        Map<String, String> headers = createHeadersMap(acaoValue, acacValue);
        headers.put("Location", String.valueOf(redirectUrl));
        awsProxyResponse.setHeaders(headers);
        return awsProxyResponse;
    }

    private AwsProxyResponse buildSessionResponse(Object session) {
        Session castedSession = (Session) session;
        Session sessionToReturn = new Session(castedSession);
        int code = HttpStatus.SC_OK;
        AwsProxyResponse awsProxyResponse = new AwsProxyResponse(code);
        awsProxyResponse.setBody(Jackson.toJsonPrettyString(new ResponseWrapper<>(code, sessionToReturn)));
        Map<String, String> headers = createHeadersMap(acaoValue, acacValue);
        headers.put(SET_COOKIE_HEADER, castedSession.getCookie());
        awsProxyResponse.setHeaders(headers);
        return awsProxyResponse;
    }

    private Map<String, String> createHeadersMap(String allowedOrigins, String allowedCredentials) {
        Map<String, String> headers = new HashMap<>(HEADERS);
        headers.put(ACAO_HEADER, allowedOrigins);
        headers.put(ACAC_HEADER, allowedCredentials);
        return headers;
    }

    private AwsProxyResponse mockOptionsRequest() {
        AwsProxyResponse awsProxyResponse = new AwsProxyResponse(HttpStatus.SC_OK);
        awsProxyResponse.setBody("{}");
        Map<String, String> headers = createHeadersMap(acaoValue, acacValue);
        headers.putAll(OPTIONS_HEADERS);
        awsProxyResponse.setHeaders(headers);
        return awsProxyResponse;
    }
}
