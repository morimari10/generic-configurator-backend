package com.se.handler;

import com.se.handler.action.Action;
import com.se.handler.module.DaggerMainApplicationComponent;
import lombok.Data;

import java.util.Map;
import javax.inject.Inject;


/**
 * Main application class for local development.
 */
@Data
public class MainApplication {

    private Map<String, Action> actionMap;

    /**
     * Default constructor. Build dagger components and then inject this in application context.
     */
    public MainApplication() {
        DaggerMainApplicationComponent.builder().build().inject(this);
    }

    @Inject
    void setActionMap(Map<String, Action> actionMap) {
        this.actionMap = actionMap;
    }

}
