package com.se.handler.module;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.easymock.PowerMock.mockStaticPartial;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * Tests for {@link ConfigModule}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigModule.class, System.class})
public class ConfigModuleTest {

    private static final String GET_ENV_METHOD_NAME = "getenv";
    private static final String ENV_VARIABLE_NAME = "env";
    private static final String TEST_ENV_NAME = "test";

    @TestSubject
    private ConfigModule configModule = new ConfigModule();

    @Test
    public void testProvideDefaultConfig() {
        assertNotNull(configModule.provideConfig());
    }

    @Test
    public void testProvideConfig() {
        mockStaticPartial(System.class, GET_ENV_METHOD_NAME);
        expect(System.getenv(ENV_VARIABLE_NAME)).andReturn(TEST_ENV_NAME);
        replayAll();
        assertNotNull(configModule.provideConfig());
        verifyAll();
    }
}
