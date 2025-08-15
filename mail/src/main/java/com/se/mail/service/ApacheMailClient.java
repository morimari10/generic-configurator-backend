package com.se.mail.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.se.common.ErrorCode;
import com.se.domain.email.Attachment;
import com.se.domain.email.SEMail;
import com.se.domain.exception.MailRuntimeException;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.mail.Session;

/**
 * The implementation of {@link MailService} service layer using apache.
 */
public class ApacheMailClient implements MailService {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Session session;
    private final String senderEmail;

    /**
     * Parametrized constructor.
     *
     * @param session     an instance of session.
     * @param senderEmail the sender email.
     */
    public ApacheMailClient(Session session, String senderEmail) {
        this.session = session;
        this.senderEmail = senderEmail;
    }

    @Override
    public void sendMail(SEMail mail) {
        LOGGER.debug("Attempt to send the following mail - [{}]", mail);
        try {

            HtmlEmail email = new HtmlEmail();
            email.setMailSession(session);
            email.setCharset(StandardCharsets.UTF_8.name());

            email.setFrom(senderEmail, mail.getSenderName());
            email.addTo(mail.getToEmail());
            email.addReplyTo(mail.getReplyEmail());

            email.setSubject(mail.getSubject());
            email.setHtmlMsg(mail.getMailBody());
            Attachment attachment = mail.getAttachment();

            LOGGER.info("Mail Object In apache Client: SenderName[{}], ToEmail[{}], ReplyEmail[{}]"
                            + "Subject[{}], MailBody[{}] ", mail.getSenderName(), mail.getToEmail(),
                    mail.getReplyEmail(), mail.getSubject(), mail.getMailBody());

            if (Objects.nonNull(attachment)) {
                email.attach(attachment.getDataSource(), attachment.getFileName(), attachment.getDescription());
            }
            email.send();

        } catch (EmailException exception) {
            ErrorCode errorCode = ErrorCode.MAIL_SEND_FAIL;
            LOGGER.error("Parameter setting error. Message error: [{}], Error code: [{}]",
                    exception.getMessage(),
                    errorCode.getKey());
            throw new MailRuntimeException(exception, errorCode.getHttpCode(), errorCode.getKey(),
                    errorCode.getDescription());
        }
    }
}
