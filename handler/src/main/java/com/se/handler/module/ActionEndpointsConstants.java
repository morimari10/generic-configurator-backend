package com.se.handler.module;

/**
 * Endpoint constants used as keys in the actions map.
 */
public final class ActionEndpointsConstants {

    /**
     * Create session endpoint.
     */
    public static final String CREATE_SESSION = "GET=session";
    /**
     * Create configuration endpoint.
     */
    public static final String CREATE_CONFIGURATION = "POST=configurations";
    /**
     * Save configuration.
     */
    public static final String SAVE_CONFIGURATION = "POST=configurations/:id";
    /**
     * Get configuration by ID.
     */
    public static final String GET_CONFIGURATION = "GET=configurations/:id";
    /**
     * Get configurations.
     */
    public static final String GET_CONFIGURATIONS = "GET=configurations";
    /**
     * Get configuration data.
     */
    public static final String CONFIGURATION_DATA = "POST=configuration-data";
    /**
     * Get configuration data.
     */
    public static final String CLIENT_AND_PROJECT = "POST=client-and-project";
    /**
     * Get site and installation data.
     */
    public static final String SITE_AND_INSTALLATION = "POST=site-and-installation";
    /**
     * Get energetic needs data.
     */
    public static final String ENERGETIC_NEEDS = "POST=energetic-needs";
    /**
     * Get energetic needs data.
     */
    public static final String REFERENCES_INFORMATION = "POST=references-information";
    /**
     * Log error event.
     */
    public static final String LOG_ERROR = "POST=log-error";

    /**
     * Endpoint for saving event data.
     */
    public static final String LOG_EVENT_ENDPOINT = "POST=events/log";

    private ActionEndpointsConstants() {
        throw new AssertionError("Constructor should not be called directly");
    }
}
