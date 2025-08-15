package com.se.mail.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.se.domain.email.Attachment;
import com.se.domain.email.SEMail;
import com.se.domain.exception.MailRuntimeException;

import java.nio.charset.StandardCharsets;
import javax.activation.DataSource;
import javax.mail.Session;

/**
 * Unit test for {@link ApacheMailClient}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ApacheMailClient.class, Session.class})
@PowerMockIgnore("javax.management.*")
public class ApacheMailClientTest {

    private static final String SENDER_EMAIL = "sender@sender.com";
    private static final String RECIPIENT_EMAIL = "recipient@recipient.com";
    private static final String REPLY_EMAIL = "reply@reply.com";
    private static final String SUBJECT = "subject";
    private static final String MAIL_BODY = "body";
    private static final String SENDER_NAME = "John Doe";
    private static final String STUB_RETURN = "stub";
    private static final String ATTACHMENT_FILE_NAME = "test.xml";

    private final HtmlEmail mockedHtmlEmail = PowerMock.createMock(HtmlEmail.class);
    private final Session mockedSession = PowerMock.createMock(Session.class);
    private final Attachment mockedAttachment = PowerMock.createMock(Attachment.class);
    private final DataSource mockedDataSource = PowerMock.createMock(DataSource.class);

    private final MailService mailService = new ApacheMailClient(mockedSession, SENDER_EMAIL);

    private SEMail inputMail;

    /**
     * Setup method.
     */
    @Before
    public void setUp() {
        inputMail = new SEMail();
        inputMail.setToEmail(RECIPIENT_EMAIL);
        inputMail.setReplyEmail(REPLY_EMAIL);
        inputMail.setSubject(SUBJECT);
        inputMail.setMailBody(MAIL_BODY);
        inputMail.setSenderName(SENDER_NAME);
        inputMail.setAttachment(mockedAttachment);
    }

    @Test
    public void testSendMail() throws Exception {
        PowerMock.expectNew(HtmlEmail.class).andReturn(mockedHtmlEmail);
        mockedHtmlEmail.setMailSession(mockedSession);
        EasyMock.expectLastCall();
        mockedHtmlEmail.setCharset(StandardCharsets.UTF_8.name());
        EasyMock.expectLastCall();
        EasyMock.expect(mockedHtmlEmail.setFrom(SENDER_EMAIL, SENDER_NAME)).andReturn(mockedHtmlEmail);
        EasyMock.expect(mockedHtmlEmail.addTo(inputMail.getToEmail())).andReturn(mockedHtmlEmail);
        EasyMock.expect(mockedHtmlEmail.addReplyTo(inputMail.getReplyEmail())).andReturn(mockedHtmlEmail);
        EasyMock.expect(mockedHtmlEmail.setSubject(inputMail.getSubject())).andReturn(mockedHtmlEmail);
        EasyMock.expect(mockedHtmlEmail.setHtmlMsg(inputMail.getMailBody())).andReturn(mockedHtmlEmail);
        EasyMock.expect(mockedAttachment.getDataSource()).andReturn(mockedDataSource);
        EasyMock.expect(mockedAttachment.getFileName()).andReturn(ATTACHMENT_FILE_NAME);
        EasyMock.expect(mockedAttachment.getDescription()).andReturn(StringUtils.EMPTY);
        EasyMock.expect(mockedHtmlEmail.attach(mockedDataSource, ATTACHMENT_FILE_NAME, StringUtils.EMPTY))
                .andReturn(mockedHtmlEmail);
        EasyMock.expect(mockedHtmlEmail.send()).andReturn(STUB_RETURN);
        PowerMock.replayAll();

        mailService.sendMail(inputMail);

        PowerMock.verifyAll();
    }

    @Test(expected = MailRuntimeException.class)
    public void testSendMailException() throws Exception {
        PowerMock.expectNew(HtmlEmail.class).andReturn(mockedHtmlEmail);

        mockedHtmlEmail.setMailSession(mockedSession);
        EasyMock.expectLastCall();
        mockedHtmlEmail.setCharset(StandardCharsets.UTF_8.name());
        EasyMock.expectLastCall();

        EasyMock.expect(mockedHtmlEmail.setFrom(SENDER_EMAIL, SENDER_NAME)).andReturn(mockedHtmlEmail);
        EasyMock.expect(mockedHtmlEmail.addTo(inputMail.getToEmail())).andThrow(new EmailException());
        PowerMock.replayAll();

        mailService.sendMail(inputMail);

        PowerMock.verifyAll();
    }
}
