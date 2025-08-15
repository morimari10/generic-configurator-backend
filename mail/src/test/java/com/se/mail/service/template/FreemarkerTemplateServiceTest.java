package com.se.mail.service.template;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import freemarker.cache.ByteArrayTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.se.domain.exception.MailRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

/**
 * Unit test for {@link FreemarkerTemplateServiceImpl}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FreemarkerTemplateServiceImpl.class, IOUtils.class})
@PowerMockIgnore("javax.management.*")
public class FreemarkerTemplateServiceTest {

    private static final Map<String, Object> MODEL = Collections.emptyMap();
    private static final String FILE_NAME = "template.file";
    private static final String FILLED_TEMPLATE = "filled template";

    private final Configuration mockedConfiguration = PowerMock.createMock(Configuration.class);
    private final Template mockedTemplate = PowerMock.createMock(Template.class);

    private final FreemarkerTemplateService templateService = new FreemarkerTemplateServiceImpl(mockedConfiguration);

    private StringWriter writer;
    private InputStream mockerInputStream;
    private ByteArrayTemplateLoader mockedByteArrayTemplateLoader;

    /**
     * Setup method.
     */
    @Before
    public void setUp() {
        writer = new StringWriter();
        writer.getBuffer().append(FILLED_TEMPLATE);
        mockerInputStream = PowerMock.createMock(InputStream.class);
        mockedByteArrayTemplateLoader = PowerMock.createMock(ByteArrayTemplateLoader.class);
    }

    @Test
    @SuppressFBWarnings("SECFREEM")
    public void testFillTemplate() throws Exception {
        PowerMock.expectNew(StringWriter.class).andReturn(writer);
        EasyMock.expect(mockedConfiguration.getTemplate(FILE_NAME)).andReturn(mockedTemplate);
        mockedTemplate.process(MODEL, writer);
        EasyMock.expectLastCall();
        PowerMock.replayAll();

        String result = templateService.fillTemplate(FILE_NAME, MODEL);

        Assert.assertEquals(FILLED_TEMPLATE, result);
        PowerMock.verifyAll();
    }

    @Test(expected = MailRuntimeException.class)
    public void testFillTemplateWithException() throws Exception {
        PowerMock.expectNew(StringWriter.class).andReturn(writer);
        EasyMock.expect(mockedConfiguration.getTemplate(FILE_NAME)).andThrow(new IOException());
        PowerMock.replayAll();

        templateService.fillTemplate(FILE_NAME, MODEL);

        PowerMock.verifyAll();
    }

    @Test
    @SuppressFBWarnings("SECFREEM")
    public void testFillTemplateWithInputStream() throws Exception {
        PowerMock.expectNew(StringWriter.class).andReturn(writer);
        PowerMock.expectNew(ByteArrayTemplateLoader.class).andReturn(mockedByteArrayTemplateLoader);
        PowerMock.mockStaticPartial(IOUtils.class, "toByteArray");
        byte[] bytes = new byte[]{};
        EasyMock.expect(IOUtils.toByteArray(mockerInputStream)).andReturn(bytes).once();
        mockedByteArrayTemplateLoader.putTemplate(FILE_NAME, bytes);
        EasyMock.expectLastCall();
        mockedConfiguration.setTemplateLoader(mockedByteArrayTemplateLoader);
        EasyMock.expectLastCall();
        EasyMock.expect(mockedConfiguration.getTemplate(FILE_NAME)).andReturn(mockedTemplate);
        mockedTemplate.process(MODEL, writer);
        EasyMock.expectLastCall();
        PowerMock.replayAll();

        String result = templateService.fillTemplate(FILE_NAME, mockerInputStream, MODEL);

        Assert.assertEquals(FILLED_TEMPLATE, result);
        PowerMock.verifyAll();
    }

    @Test(expected = MailRuntimeException.class)
    public void testFillTemplateWithInputStreamFailed() throws Exception {
        PowerMock.expectNew(StringWriter.class).andReturn(writer);
        PowerMock.expectNew(ByteArrayTemplateLoader.class).andReturn(mockedByteArrayTemplateLoader);
        PowerMock.mockStaticPartial(IOUtils.class, "toByteArray");
        byte[] bytes = new byte[]{};
        EasyMock.expect(IOUtils.toByteArray(mockerInputStream)).andReturn(bytes).once();
        mockedByteArrayTemplateLoader.putTemplate(FILE_NAME, bytes);
        EasyMock.expectLastCall();
        mockedConfiguration.setTemplateLoader(mockedByteArrayTemplateLoader);
        EasyMock.expectLastCall();
        EasyMock.expect(mockedConfiguration.getTemplate(FILE_NAME)).andThrow(new IOException());
        PowerMock.replayAll();

        templateService.fillTemplate(FILE_NAME, mockerInputStream, MODEL);

        PowerMock.verifyAll();
    }
}
