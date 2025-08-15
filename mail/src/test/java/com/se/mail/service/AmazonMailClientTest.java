package com.se.mail.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.AmazonSimpleEmailServiceException;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.se.domain.email.SEMail;
import com.se.domain.exception.MailRuntimeException;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link AmazonMailClient}.
 */
public class AmazonMailClientTest {

    private static final String SENDER_EMAIL = "sender@sender.com";
    private static final String RECIPIENT_EMAIL = "recipient@recipient.com";
    private static final String REPLY_EMAIL = "reply@reply.com";
    private static final String SUBJECT = "subject";
    private static final String MAIL_BODY = "body";
    private static final String SENDER_NAME = "John Doe";
    private static final String EXCEPTION_MESSAGE = "exception message";

    private final AmazonSimpleEmailService mockedAmazonSES = EasyMock.createMock(AmazonSimpleEmailService.class);
    private final SendRawEmailResult stubResult = EasyMock.createNiceMock(SendRawEmailResult.class);

    private final MailService amazonMailClient = new AmazonMailClient(mockedAmazonSES, SENDER_EMAIL);

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
    }

    @Test
    public void testSendMail() {
        Capture<SendRawEmailRequest> capturedArg = EasyMock.newCapture();

        SendRawEmailRequest expectedRequestWithCapture =
                EasyMock.and(EasyMock.capture(capturedArg), EasyMock.isA(SendRawEmailRequest.class));
        EasyMock.expect(mockedAmazonSES.sendRawEmail(expectedRequestWithCapture)).andReturn(stubResult);
        EasyMock.replay(mockedAmazonSES);

        amazonMailClient.sendMail(inputMail);

        EasyMock.verify(mockedAmazonSES);

        SendRawEmailRequest capturedRequest = capturedArg.getValue();

        Assert.assertTrue(capturedRequest.getDestinations().contains(RECIPIENT_EMAIL));
    }

    @Test(expected = MailRuntimeException.class)
    public void testSendMailException() {
        EasyMock.expect(mockedAmazonSES.sendRawEmail(EasyMock.anyObject()))
                .andThrow(new AmazonSimpleEmailServiceException(EXCEPTION_MESSAGE));
        EasyMock.replay(mockedAmazonSES);

        amazonMailClient.sendMail(inputMail);

        EasyMock.verify(mockedAmazonSES);
    }
}
