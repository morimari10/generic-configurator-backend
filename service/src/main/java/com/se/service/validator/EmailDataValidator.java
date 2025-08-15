package com.se.service.validator;

import com.se.common.ErrorCode;
import com.se.domain.email.EmailData;
import com.se.domain.exception.ServiceException;

/**
 * Validator for the {@link EmailData}.
 */
public class EmailDataValidator extends Validator<EmailData> {

    private EmailFormFieldsValidator emailFormFieldsValidator;
    private RecaptchaValidator recaptchaValidator;
    private ConfigurationStatusValidator configurationStatusValidator;


    /**
     * Parametrized constructor.
     *
     * @param formFieldsValidator          {@link EmailFormFieldsValidator} instance.
     * @param recaptchaValidator           {@link RecaptchaValidator} instance.
     * @param emailProductsLinksValidator  {@link EmailProductsLinksValidator} instance.
     * @param configurationStatusValidator {@link ConfigurationStatusValidator} instance.
     */
    public EmailDataValidator(EmailFormFieldsValidator formFieldsValidator, RecaptchaValidator recaptchaValidator,
                              ConfigurationStatusValidator configurationStatusValidator) {
        this.emailFormFieldsValidator = formFieldsValidator;
        this.recaptchaValidator = recaptchaValidator;
        this.configurationStatusValidator = configurationStatusValidator;
    }

    /**
     * email data and recaptch and product link validation.
     *
     * @param emailData  email data and recaptch and product link validation.
     * @return
     */
    public boolean validate(EmailData emailData) {
        if (!configurationStatusValidator.validate(emailData.getConfigurationStatus())) {
            throw new ServiceException(ErrorCode.UNSUPPORTED_CONFIG_STATUS);
        }
        if (!recaptchaValidator.validate(emailData.getCaptchaResponse())) {
            throw new ServiceException(ErrorCode.INVALID_RECAPTCHA_RESPONSE);
        }
        if (!emailFormFieldsValidator.validate(emailData)) {
            throw new ServiceException(ErrorCode.INVALID_REQUEST);
        }
        return true;
    }
}
