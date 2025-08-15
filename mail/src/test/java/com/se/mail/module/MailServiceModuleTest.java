package com.se.mail.module;

import com.se.domain.type.MailClientImplementationType;
import com.se.mail.service.AmazonMailClient;
import com.se.mail.service.ApacheMailClient;
import com.se.mail.service.MailService;

import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;
import javax.mail.Authenticator;

/**
 * Tests for {@link MailServiceModule}.
 */
public class MailServiceModuleTest {

    private static final Properties MAIL_PROPS = new Properties();
    private static final String MAIL_FROM = "mailFrom";
    private static final MailClientImplementationType AWS_IMPL = MailClientImplementationType.AWS;
    private static final MailClientImplementationType APACHE_IMPL = MailClientImplementationType.APACHE;
    private static final MailClientImplementationType LOCAL_IMPL = MailClientImplementationType.LOCAL;
    private static final String REGION = "us-east-1";
    private static final String URL = "http://localhost:4579";

    @Mock
    private Authenticator mockedAuthenticator;

    @TestSubject
    private MailServiceModule mailServiceModule = new MailServiceModule();

    @Test
    public void testProvideMailServiceAmazon() {
        MailService actualResult = mailServiceModule.provideMailService(MAIL_PROPS, MAIL_FROM, AWS_IMPL, REGION, URL,
                mockedAuthenticator);

        Assert.assertNotNull(actualResult);
        Assert.assertTrue(actualResult instanceof AmazonMailClient);
    }

    @Test
    public void testProvideMailServiceApache() {
        MailService actualResult = mailServiceModule.provideMailService(MAIL_PROPS, MAIL_FROM, APACHE_IMPL, REGION, URL,
                mockedAuthenticator);

        Assert.assertNotNull(actualResult);
        Assert.assertTrue(actualResult instanceof ApacheMailClient);
    }

    @Test
    public void testProvideMailServiceLocal() {
        MailService actualResult = mailServiceModule.provideMailService(MAIL_PROPS, MAIL_FROM, LOCAL_IMPL, REGION, URL,
                mockedAuthenticator);

        Assert.assertNotNull(actualResult);
        Assert.assertTrue(actualResult instanceof AmazonMailClient);
    }
}
