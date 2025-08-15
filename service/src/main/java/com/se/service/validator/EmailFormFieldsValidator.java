package com.se.service.validator;

import org.apache.commons.lang3.StringUtils;

import com.se.domain.configuration.ConfigurationStatus;
import com.se.domain.email.EmailData;

import java.util.Objects;
import java.util.Optional;

/**
 * Validator for the form fields of {@link EmailData}.
 */
public class EmailFormFieldsValidator extends Validator<EmailData> {

    private String fieldLengthRegexp;
    private String emailRegexp;
    private String messageLengthRegexp;

    /**
     * Parametrized constructor.
     *
     * @param fieldLengthRegexp   field length pattern
     * @param emailRegexp         email pattern
     * @param messageLengthRegexp message length pattern
     */
    public EmailFormFieldsValidator(String fieldLengthRegexp, String emailRegexp, String messageLengthRegexp) {
        this.fieldLengthRegexp = fieldLengthRegexp;
        this.emailRegexp = emailRegexp;
        this.messageLengthRegexp = messageLengthRegexp;
    }

    @Override
    public boolean validate(EmailData emailData) {
        Validator<String> lengthValidator = new StringMatchValidator(fieldLengthRegexp);
        Validator<String> emailValidator = new StringMatchValidator(emailRegexp);
        Validator<String> messageValidator = new StringMatchValidator(messageLengthRegexp);
        String senderName = emailData.getSenderName();
        String senderEmail = emailData.getSenderEmail();
        if (Objects.equals(ConfigurationStatus.IN_PROGRESS, emailData.getConfigurationStatus())) {
            String recipientName = Optional.ofNullable(emailData.getRecipientName()).orElse(StringUtils.EMPTY);
            String recipientEmail = Optional.ofNullable(emailData.getRecipientEmail()).orElse(StringUtils.EMPTY);
            String message = Optional.ofNullable(emailData.getMessage()).orElse(StringUtils.EMPTY);
            return lengthValidator.validateAll(recipientName, recipientEmail, senderName, senderEmail)
                    && emailValidator.validateAll(recipientEmail, senderEmail)
                    && messageValidator.validate(message);
        } else {
            return lengthValidator.validateAll(senderName, senderEmail)
                    && emailValidator.validateAll(senderEmail);
        }

    }
}
