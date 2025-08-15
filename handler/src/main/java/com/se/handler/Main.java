package com.se.handler;

import static spark.Spark.afterAfter;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.patch;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import spark.Request;
import spark.Response;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.se.common.SERuntimeException;
import com.se.domain.session.Session;
import com.se.handler.action.Action;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Main class for local development.
 */
public final class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String ACCESS_CONTROL_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_ORIGIN = "Access-Control-Allow-Origin";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String GET_OPTIONS = "GET,OPTIONS,POST,PUT,PATCH,DELETE";
    private static final String ACAC_HEADER = "Access-Control-Allow-Credentials";
    private static final String APPLICATION_JSON = "application/json";
    private static final String SET_COOKIE_HEADER = "Set-Cookie";
    private static final String SESSION_ROUTE = "session";

    private static final String UNKNOWN_ERROR_CODE_STR = "520";
    private static final Integer UNKNOWN_ERROR_CODE = 520;
    private static final Integer PORT = 3635;

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "DELETE";


    private Main() {
        throw new AssertionError("Constructor should not be called directly");
    }

    private static Map<String, String> buildHeaders(Request req) {
        Map<String, String> result = new HashMap<>();
        for (String headerKey : req.headers()) {
            result.put(headerKey, req.headers(headerKey));
        }
        return result;
    }

    private static Map<String, String> buildQueryParams(Request req) {
        Map<String, String> result = new HashMap<>();
        for (String paramKey : req.queryParams()) {
            result.put(paramKey, req.queryParams(paramKey));
        }
        return result;
    }

    private static Map<String, String> buildPathParameters(Request req) {
        Map<String, String> result = new HashMap<>();
        for (Entry<String, String> entry : req.params().entrySet()) {
            result.put(entry.getKey().split(":")[1], entry.getValue());
        }
        return result;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    private static Object handleActionExecution(AwsProxyRequest wrapper, Action action,
        String routeKey,
        Response response) {
        try {
            Object result = action.perform(wrapper);
            if (routeKey.equals(SESSION_ROUTE)) {
                Session castedSession = (Session) result;
                response.header(SET_COOKIE_HEADER, castedSession.getCookie());
            }

            return buildSuccessfulResponse(result);
        } catch (SERuntimeException exception) {
            LOGGER.error("Occurs the error with following code - [{}] and error template - [{}]",
                    exception.getErrorCode(), exception.getErrorTemplate(), exception);
            response.status(exception.getHttpCode());
            return buildErrorResponse(exception.getErrorCode(), null);
        } catch (Exception exception) {
            LOGGER.error("Occurs an unknown error", exception);
            response.status(UNKNOWN_ERROR_CODE);
            return buildErrorResponse(UNKNOWN_ERROR_CODE_STR, null);
        }
    }

     private static Map<String, Object> buildSuccessfulResponse(Object payload) {
        Map<String, Object> result = new HashMap<>();
        result.put("statusCode", HttpStatus.SC_OK);
        result.put("payload", payload);
        return result;
    }

    private static Map<String, Object> buildErrorResponse(String errorCode, List<String> values) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("errorCode", errorCode);
        payload.put("values", values);
        return payload;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    private static void bypassSSLCertificateVerification() {
        LOGGER.debug("Creating Trust Manager to Bypass SSL Certificate Verification ...");
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }
    }

    /**
     * Application entrypoint.
     *
     * @param args arguments
     */
    @SuppressWarnings("uncommentedmain")
    public static void main(String[] args) {
        MainApplication application = new MainApplication();
        bypassSSLCertificateVerification();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        port(PORT);

        Set<String> optionRoutesToCreate = new HashSet<>();

        for (Entry<String, Action> entry : application.getActionMap().entrySet()) {
            String[] methodAndRoute = entry.getKey().split("=");
            
            if (methodAndRoute.length > 1) {
                String method = methodAndRoute[0];
                String route = methodAndRoute[1];

                switch (method) {
                    case GET:
                        get(route, (req, res) -> {
                            res.type(APPLICATION_JSON);

                            AwsProxyRequest request = new AwsProxyRequest();
                            request.setHeaders(buildHeaders(req));
                            request.setQueryStringParameters(buildQueryParams(req));
                            request.setPathParameters(buildPathParameters(req));

                            return handleActionExecution(request, entry.getValue(), route, res);
                        }, result -> {
                                return new ObjectMapper()
                                    .setSerializationInclusion(Include.NON_NULL)
                                    .writeValueAsString(result); });
                        break;
                    case POST:
                        optionRoutesToCreate.add(route);
                        post(route, (req, res) -> {
                            res.type(APPLICATION_JSON);
                            AwsProxyRequest request = new AwsProxyRequest();
                            request.setHeaders(buildHeaders(req));
                            request.setQueryStringParameters(buildQueryParams(req));
                            request.setPathParameters(buildPathParameters(req));
                            request.setBody(req.body());

                            return handleActionExecution(request, entry.getValue(), route, res);
                        }, result -> {
                                return new ObjectMapper()
                                    .setSerializationInclusion(Include.NON_NULL)
                                    .writeValueAsString(result); });
                        break;
                    case PUT:
                        optionRoutesToCreate.add(route);
                        put(route, (req, res) -> {
                            res.type(APPLICATION_JSON);
                            AwsProxyRequest request = new AwsProxyRequest();
                            request.setHeaders(buildHeaders(req));
                            request.setQueryStringParameters(buildQueryParams(req));
                            request.setPathParameters(buildPathParameters(req));
                            request.setBody(req.body());

                            return handleActionExecution(request, entry.getValue(), route, res);
                        }, result -> {
                                return new ObjectMapper()
                                    .setSerializationInclusion(Include.NON_NULL)
                                    .writeValueAsString(result); });
                        break;
                    case PATCH:
                        optionRoutesToCreate.add(route);
                        patch(route, (req, res) -> {
                            res.type(APPLICATION_JSON);
                            AwsProxyRequest request = new AwsProxyRequest();
                            request.setHeaders(buildHeaders(req));
                            request.setQueryStringParameters(buildQueryParams(req));
                            request.setPathParameters(buildPathParameters(req));
                            request.setBody(req.body());

                            return handleActionExecution(request, entry.getValue(), route, res);
                        }, result -> {
                                return new ObjectMapper()
                                    .setSerializationInclusion(Include.NON_NULL)
                                    .writeValueAsString(result); });
                        break;
                    case DELETE:
                        optionRoutesToCreate.add(route);
                        delete(route, (req, res) -> {
                            res.type(APPLICATION_JSON);
                            AwsProxyRequest request = new AwsProxyRequest();
                            request.setHeaders(buildHeaders(req));
                            request.setQueryStringParameters(buildQueryParams(req));
                            request.setPathParameters(buildPathParameters(req));
                            request.setBody(req.body());

                            return handleActionExecution(request, entry.getValue(), route, res);
                        }, result -> {
                                return new ObjectMapper()
                                    .setSerializationInclusion(Include.NON_NULL)
                                    .writeValueAsString(result); });
                        break;
                    default:
                        break;
                }
            }
        }

        for (String route : optionRoutesToCreate) {
            options(route, (req, res) -> {
                res.header(ACCESS_CONTROL_METHODS, GET_OPTIONS);
                res.header(ACCESS_CONTROL_HEADERS, CONTENT_TYPE);

                res.type("");
                return true;
            }, gson::toJson);
        }

        afterAfter((request, response) -> {
            response.header(ACCESS_CONTROL_ORIGIN, Optional
                .ofNullable(request.headers("Origin")).orElse(request.headers("origin")));
            response.header(ACAC_HEADER, "true");
        });
    }
}
