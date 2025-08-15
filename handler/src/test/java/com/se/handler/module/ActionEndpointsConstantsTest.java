package com.se.handler.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Tests for {@link ActionEndpointsConstants}.
 */
public class ActionEndpointsConstantsTest {

    private static final String EXPECTED_ERROR_MESSAGE = "Constructor should not be called directly";

    @Test
    public void testConstructor() throws NoSuchMethodException {
        Constructor<ActionEndpointsConstants> constantsConstructor = ActionEndpointsConstants.class
                .getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constantsConstructor.getModifiers()));
        try {
            constantsConstructor.setAccessible(true);
            constantsConstructor.newInstance();
        } catch (InvocationTargetException exc) {
            Throwable originalException = exc.getCause();
            assertNotNull(originalException);
            assertTrue(originalException instanceof AssertionError);
            assertTrue(StringUtils.equals(EXPECTED_ERROR_MESSAGE, originalException.getMessage()));
        } catch (Exception exc) {
            fail();
        } finally {
            constantsConstructor.setAccessible(false);
        }
    }
}
