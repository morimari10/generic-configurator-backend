package com.se.mail.module;

import com.se.domain.type.MailClientImplementationType;
import com.typesafe.config.Config;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Properties;
import javax.mail.Authenticator;

/**
 * Tests for {@link MailPropertiesModule}.
 */
@RunWith(EasyMockRunner.class)
public class MailPropertiesModuleTest {

    private static final String MAIL_SMTP_HOST_KEY = "mail.smtp.host";
    private static final String MAIL_SMTP_HOST_VALUE = "mail-smtp-host";
    //private static final String MAIL_SMTP_SOCKET_FACTORY_PORT_KEY = "mail.smtp.socketFactory.port";
    //private static final String MAIL_SMTP_SOCKET_FACTORY_PORT_VALUE = "mail-smtp-socketFactory-port";
    //private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS_KEY = "mail.smtp.socketFactory.class";
    //private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS_VALUE = "mail-smtp-socketFactory-class";
    private static final String MAIL_SMTP_AUTH_KEY = "mail.smtp.auth";
    private static final String MAIL_SMTP_AUTH_VALUE = "mail-smtp-auth";
    private static final String MAIL_SMTP_START_TTLS_ENABLE_KEY = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_START_TTLS_ENABLE_VALUE = "mail-smtp-starttls-enable";
    private static final String MAIL_SMTP_SSL_TRUST_KEY = "mail.smtp.ssl.trust";
    private static final String MAIL_SMTP_SSL_TRUST_VALUE = "mail.smtp.ssl.trust";
    private static final String MAIL_SMTP_PORT_KEY = "mail.smtp.port";
    private static final String MAIL_SMTP_PORT_VALUE = "mail-smtp-port";
    private static final String MAIL_SMTP_SSL_PROTOCOLS_KEY = "mail.smtp.ssl.protocols";
    private static final String MAIL_SMTP_SSL_PROTOCOLS_VALUE = "mail.smtp.ssl.protocols";
    private static final String MAIL_USERNAME_KEY = "mail.username";
    private static final String MAIL_USERNAME_VALUE = "mail-username";
    private static final String MAIL_PASSWORD_KEY = "mail.password";
    private static final String MAIL_PASSWORD_VALUE = "mail-password";
    private static final String MAIL_FROM_KEY = "mail.from";
    private static final String MAIL_FROM_VALUE = "mail-from";
    private static final String MAIL_IMPLEMENTATION_KEY = "mail.implementation";
    private static final String MAIL_IMPLEMENTATION_VALUE = "aws";
    private static final String MAIL_AMAZON_REGION_KEY = "mail.amazon.region";
    private static final String MAIL_AMAZON_REGION_VALUE = "mail-amazon-region";
    private static final String MAIL_AMAZON_URL_KEY = "mail.amazon.url";
    private static final String MAIL_AMAZON_URL_VALUE = "mail-amazon-url";


    @Mock
    private Config mockedConfig;

    @TestSubject
    private MailPropertiesModule mailPropertiesModule = new MailPropertiesModule();

    @Test
    public void testProvideMailProperties() {
        Properties expectedResult = new Properties();
        expectedResult.setProperty(MAIL_SMTP_HOST_KEY, MAIL_SMTP_HOST_VALUE);
        //expectedResult.setProperty(MAIL_SMTP_SOCKET_FACTORY_PORT_KEY, MAIL_SMTP_SOCKET_FACTORY_PORT_VALUE);
        //expectedResult.setProperty(MAIL_SMTP_SOCKET_FACTORY_CLASS_KEY, MAIL_SMTP_SOCKET_FACTORY_CLASS_VALUE);
        expectedResult.setProperty(MAIL_SMTP_AUTH_KEY, MAIL_SMTP_AUTH_VALUE);
        expectedResult.setProperty(MAIL_SMTP_START_TTLS_ENABLE_KEY, MAIL_SMTP_START_TTLS_ENABLE_VALUE);
        expectedResult.setProperty(MAIL_SMTP_SSL_TRUST_KEY, MAIL_SMTP_SSL_TRUST_VALUE);
        expectedResult.setProperty(MAIL_SMTP_PORT_KEY, MAIL_SMTP_PORT_VALUE);
        expectedResult.setProperty(MAIL_SMTP_SSL_PROTOCOLS_KEY, MAIL_SMTP_SSL_PROTOCOLS_VALUE);
        expectedResult.setProperty(MAIL_USERNAME_KEY, MAIL_USERNAME_VALUE);
        expectedResult.setProperty(MAIL_PASSWORD_KEY, MAIL_PASSWORD_VALUE);
        expectedResult.setProperty(MAIL_FROM_KEY, MAIL_FROM_VALUE);
        expectedResult.setProperty(MAIL_IMPLEMENTATION_KEY, MAIL_IMPLEMENTATION_VALUE);


        EasyMock.expect(mockedConfig.getString(MAIL_SMTP_HOST_KEY)).andReturn(MAIL_SMTP_HOST_VALUE).once();
        //EasyMock.expect(mockedConfig.getString(MAIL_SMTP_SOCKET_FACTORY_PORT_KEY))
        // .andReturn(MAIL_SMTP_SOCKET_FACTORY_PORT_VALUE).once();
        //EasyMock.expect(mockedConfig.getString(MAIL_SMTP_SOCKET_FACTORY_CLASS_KEY))
        //  .andReturn(MAIL_SMTP_SOCKET_FACTORY_CLASS_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_SMTP_AUTH_KEY)).andReturn(MAIL_SMTP_AUTH_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_SMTP_START_TTLS_ENABLE_KEY))
                .andReturn(MAIL_SMTP_START_TTLS_ENABLE_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_SMTP_SSL_TRUST_KEY))
                .andReturn(MAIL_SMTP_SSL_TRUST_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_SMTP_PORT_KEY)).andReturn(MAIL_SMTP_PORT_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_SMTP_SSL_PROTOCOLS_KEY))
                .andReturn(MAIL_SMTP_SSL_PROTOCOLS_KEY).once();
        EasyMock.expect(mockedConfig.getString(MAIL_USERNAME_KEY)).andReturn(MAIL_USERNAME_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_PASSWORD_KEY)).andReturn(MAIL_PASSWORD_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_FROM_KEY)).andReturn(MAIL_FROM_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_IMPLEMENTATION_KEY))
                .andReturn(MAIL_IMPLEMENTATION_VALUE).once();


        EasyMock.replay(mockedConfig);
        Properties actualResult = mailPropertiesModule.provideMailProperties(mockedConfig);
        Assert.assertEquals(expectedResult, actualResult);
        EasyMock.verify(mockedConfig);
    }

    @Test
    public void testProvideMailAuthenticator() {
        EasyMock.expect(mockedConfig.getString(MAIL_USERNAME_KEY)).andReturn(MAIL_USERNAME_VALUE).once();
        EasyMock.expect(mockedConfig.getString(MAIL_PASSWORD_KEY)).andReturn(MAIL_PASSWORD_VALUE).once();
        EasyMock.replay(mockedConfig);
        Authenticator actualResult = mailPropertiesModule.provideMailAuthenticator(mockedConfig);
        Assert.assertNotNull(actualResult);
        EasyMock.verify(mockedConfig);
    }

    @Test
    public void testProvideFromMailProperty() {
        String expectedResult = "mail-from";
        EasyMock.expect(mockedConfig.getString(MAIL_FROM_KEY)).andReturn(MAIL_FROM_VALUE).once();
        EasyMock.replay(mockedConfig);
        String actualResult = mailPropertiesModule.provideFromMailProperty(mockedConfig);
        Assert.assertEquals(expectedResult, actualResult);
        EasyMock.verify(mockedConfig);
    }

    @Test
    public void testProvideMailServiceImplementation() {
        MailClientImplementationType expectedResult = MailClientImplementationType.AWS;
        EasyMock.expect(mockedConfig.getString(MAIL_IMPLEMENTATION_KEY)).andReturn(MAIL_IMPLEMENTATION_VALUE).once();
        EasyMock.replay(mockedConfig);
        MailClientImplementationType actualResult = mailPropertiesModule.provideMailServiceImplementation(mockedConfig);
        Assert.assertEquals(expectedResult, actualResult);
        EasyMock.verify(mockedConfig);
    }

    @Test
    public void testProvideMailAmazonRegion() {
        String expectedResult = "mail-amazon-region";
        EasyMock.expect(mockedConfig.getString(MAIL_AMAZON_REGION_KEY)).andReturn(MAIL_AMAZON_REGION_VALUE).once();
        EasyMock.replay(mockedConfig);
        String actualResult = mailPropertiesModule.provideMailAmazonRegion(mockedConfig);
        Assert.assertEquals(expectedResult, actualResult);
        EasyMock.verify(mockedConfig);
    }

    @Test
    public void testProvideMailAmazonUrl() {
        String expectedResult = "mail-amazon-url";
        EasyMock.expect(mockedConfig.getString(MAIL_AMAZON_URL_KEY)).andReturn(MAIL_AMAZON_URL_VALUE).once();
        EasyMock.replay(mockedConfig);
        String actualResult = mailPropertiesModule.provideMailAmazonUrl(mockedConfig);
        Assert.assertEquals(expectedResult, actualResult);
        EasyMock.verify(mockedConfig);
    }

}
