package com.se.mail.module;

import freemarker.template.Configuration;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.se.mail.service.template.FreemarkerTemplateService;

/**
 * Tests for {@link TemplateModule}.
 */
@RunWith(EasyMockRunner.class)
public class TemplateModuleTest {

    @Mock
    private Configuration mockedConfiguration;

    @TestSubject
    private TemplateModule templateModule = new TemplateModule();

    @Test
    public void testProvideTemplateService() {
        FreemarkerTemplateService actualResult = templateModule.provideTemplateService(mockedConfiguration);
        Assert.assertNotNull(actualResult);
    }
}
