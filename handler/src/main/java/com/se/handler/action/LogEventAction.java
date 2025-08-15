package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.se.domain.configuration.event.EventData;
import com.se.service.event.EventService;

import java.util.Map;

/**
 * The implementation of {@link Action} for saving event data.
 */
public class LogEventAction extends AbstractAction<EventData> {

    private EventService eventService;

    /**
     * Parametrized constructor.
     *
     * @param eventService service for saving event data.
     */
    public LogEventAction(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public EventData perform(String body, Map<String, String> query) {
        return eventService.log(body);
    }

    @Override
    public EventData perform(AwsProxyRequest request, String s) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
