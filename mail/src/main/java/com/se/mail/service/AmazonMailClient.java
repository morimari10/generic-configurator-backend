package com.se.mail.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.AmazonSimpleEmailServiceException;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.se.common.ErrorCode;
import com.se.domain.email.Attachment;
import com.se.domain.email.SEMail;
import com.se.domain.exception.MailRuntimeException;

import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * The implementation of {@link MailService} service layer using Amazon SES.
 */
public class AmazonMailClient implements MailService {

    private static final Logger LOGGER = LogManager.getLogger(AmazonMailClient.class);

    private static final String FROM_TEMPLATE = "${senderName} <${senderEmail}>";

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final String senderEmail;

    /**
     * Parametrized constructor.
     *
     * @param amazonSimpleEmailService the Amazon SES instance
     * @param senderEmail              the sender email
     */
    @Inject
    public AmazonMailClient(AmazonSimpleEmailService amazonSimpleEmailService, String senderEmail) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
        this.senderEmail = senderEmail;
    }

    @Override
    public void sendMail(SEMail mail) {
        LOGGER.debug("Attempt to send the following mail - [{}]", mail);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<String, String> replaceMap = ImmutableMap.<String, String>builder()
                    .put("senderName", MimeUtility.encodeWord(mail.getSenderName()))
                    .put("senderEmail", senderEmail)
                    .build();
            String from = StringSubstitutor.replace(FROM_TEMPLATE, replaceMap);

            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            message.setSubject(mail.getSubject(), StandardCharsets.UTF_8.name());
            message.setFrom(from);
            message.setReplyTo(new InternetAddress[]{new InternetAddress(mail.getReplyEmail())});
            MimeMultipart mimeBodyPart = new MimeMultipart();
            BodyPart part = new MimeBodyPart();
            part.setContent(mail.getMailBody(), "text/html; charset=UTF-8");
            mimeBodyPart.addBodyPart(part);
            Attachment attachment = mail.getAttachment();
            if (Objects.nonNull(attachment)) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.setDataHandler(new DataHandler(attachment.getDataSource()));
                attachmentBodyPart.setFileName(attachment.getFileName());
                mimeBodyPart.addBodyPart(attachmentBodyPart);
            }
            message.setContent(mimeBodyPart);
            message.writeTo(outputStream);
            RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
            rawEmailRequest.setDestinations(Lists.newArrayList(mail.getToEmail()));
            rawEmailRequest.setSource(from);

            amazonSimpleEmailService.sendRawEmail(rawEmailRequest);
        } catch (AmazonSimpleEmailServiceException | MessagingException | IOException exception) {
            ErrorCode errorCode = ErrorCode.MAIL_SEND_FAIL;
            LOGGER.error("Parameter setting error. Message error: [{}], Error code: [{}]",
                    exception.getMessage(),
                    errorCode.getKey());
            throw new MailRuntimeException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }
}
