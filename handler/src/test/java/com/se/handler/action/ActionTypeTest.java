package com.se.handler.action;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Tests for {@link ActionType}.
 */
public class ActionTypeTest {

    private static final String DEFAULT = "DEFAULT";
    private static final String EL_BRIDGE = "EL_BRIDGE";

    @Test
    public void testActionType() {
        assertEquals(DEFAULT, ActionType.DEFAULT.name());
        assertEquals(EL_BRIDGE, ActionType.EL_BRIDGE.name());
    }
}
