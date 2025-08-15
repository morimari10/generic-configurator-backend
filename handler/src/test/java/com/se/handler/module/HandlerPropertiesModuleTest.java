package com.se.handler.module;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Tests for {@link HandlerPropertiesModule}.
 */
@RunWith(EasyMockRunner.class)
public class HandlerPropertiesModuleTest {

    private static final String ALLOWED_ORIGINS_PROPERTY = "allowedOrigins";
    private static final String ALLOWED_ORIGIN_1 = "https://schneider-electric.com";
    private static final String ALLOWED_ORIGIN_2 = "https://se.com";
    private static final String ALLOWED_ORIGINS_VALUE = ALLOWED_ORIGIN_1 + "," + ALLOWED_ORIGIN_2;

    @Mock
    private Config mockedConfig;

    @TestSubject
    private HandlerPropertiesModule handlerPropertiesModule = new HandlerPropertiesModule();

    @Test
    public void testProvideAllowedOrigins() {
        expect(mockedConfig.getString(ALLOWED_ORIGINS_PROPERTY)).andReturn(ALLOWED_ORIGINS_VALUE);
        replay(mockedConfig);
        List<String> expectedResult = Lists.newArrayList(ALLOWED_ORIGIN_1, ALLOWED_ORIGIN_2);
        List<String> actualResult = handlerPropertiesModule.provideAllowedOrigins(mockedConfig);
        assertEquals(expectedResult, actualResult);
        verify(mockedConfig);
    }
}
