package com.se.mail.module;

import freemarker.template.Configuration;
import org.easymock.EasyMockRunner;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DefaultFreemarkerConfigurationModule}.
 */
@RunWith(EasyMockRunner.class)
public class DefaultFreemarkerConfigurationModuleTest {

    @TestSubject
    private DefaultFreemarkerConfigurationModule defaultFreemarkerConfigurationModule =
            new DefaultFreemarkerConfigurationModule();

    @Test
    public void testProvideConfiguration() {
        Configuration actualResult = defaultFreemarkerConfigurationModule.provideConfiguration();
        Assert.assertNotNull(actualResult);
    }
}
