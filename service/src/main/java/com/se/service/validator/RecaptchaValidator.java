package com.se.service.validator;

import com.amazonaws.util.json.Jackson;
import com.se.common.ErrorCode;
import com.se.domain.email.CaptchaResponse;
import com.se.domain.exception.ServiceException;

import dagger.Lazy;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;
import javax.ws.rs.core.MediaType;

/**
 * Validator for recaptcha client response.
 */
public class RecaptchaValidator extends Validator<String> {

    private static final Logger LOGGER = LogManager.getLogger(RecaptchaValidator.class);

    private static final String SECRET_PARAM_NAME = "secret";
    private static final String RESPONSE_PARAM_NAME = "response";

    private String captchaVerificationUrl;
    private String captchaSecretKey;
    private Lazy<OkHttpClient> restClient;

    /**
     * Parametrized constructor.
     *
     * @param restClient             the rest client instance.
     * @param captchaVerificationUrl the recaptcha verification url.
     * @param captchaSecretKey       the recaptcha secret key.
     */
    public RecaptchaValidator(Lazy<OkHttpClient> restClient, String captchaVerificationUrl, String captchaSecretKey) {
        this.restClient = restClient;
        this.captchaVerificationUrl = captchaVerificationUrl;
        this.captchaSecretKey = captchaSecretKey;
    }

    @Override
    public boolean validate(String captchaClientResponse) {
        try (Response response = callReCaptchaService(captchaClientResponse)) {
            LOGGER.debug("#####---- Captcha Response [{}]", response);
            LOGGER.debug("#####----captchaClientResponse  [{}]", captchaClientResponse);

            if (response.isSuccessful()) {
                CaptchaResponse captchaServerResponse =
                        Jackson.fromJsonString(Objects.requireNonNull(response.body()).string(), CaptchaResponse.class);
                response.close();

                LOGGER.debug("#####----server Response [{}]", captchaServerResponse);

                return captchaServerResponse.isSuccessful();
            }
        } catch (IOException exception) {
            LOGGER.error("Error an occurred during sending request to google recaptcha resource");
            throw new ServiceException(ErrorCode.ERROR_CAPTCHA_SERVICE_UNAVAILABLE);
        }
        return false;
    }

    private Response callReCaptchaService(String captchaClientResponse) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(captchaVerificationUrl)).newBuilder();
        urlBuilder.addQueryParameter(SECRET_PARAM_NAME, captchaSecretKey);
        urlBuilder.addQueryParameter(RESPONSE_PARAM_NAME, captchaClientResponse);

        LOGGER.debug("#####----url Response [{}]", urlBuilder.build().toString());
        LOGGER.debug("#####----captchaSecretKey [{}]", captchaSecretKey);

        RequestBody body = RequestBody.create(new byte[0]);
        LOGGER.debug("#####----RequestBody captcha [{}]", body);
        LOGGER.debug("#####----captchaClient Response [{}]", captchaClientResponse);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .method("POST", body)
                .build();
        try {
            return restClient.get().newCall(request).execute();
        } catch (IOException exception) {
            LOGGER.error("Error an occurred during sending request to google recaptcha resource");
            throw new ServiceException(ErrorCode.ERROR_CAPTCHA_SERVICE_UNAVAILABLE);
        }
    }
}
