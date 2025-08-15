package com.se.service.event;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import com.se.domain.configuration.event.EventData;
import com.se.storage.dao.EventDao;

import java.util.Date;
import java.util.UUID;

/**
 * Service for event logging.
 */
public class EventService {

    private EventDao eventDao;

    /**
     * Parametrized constructor.
     *
     * @param eventDao the event dao instance.
     */
    public EventService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    /**
     * Saves an event using configuration id, event type and optional meta info from
     * json body.
     *
     * @param body json body with configuration id, event type and optional meta
     *             info.
     * @return saved {@link EventData}.
     */
    public EventData log(String body) {
        EventData eventData = Jackson.fromJsonString(body, EventData.class);
        return log(eventData.getConfigurationId(), eventData.getEventType(), eventData.getTag(),
                eventData.getMetaInfo());
    }

    /**
     * Saves an event using specified configuration id, event type and meta info.
     *
     * @param configurationId configuration id.
     * @param eventType       event type.
     * @param metaInfo        additional meta info.
     * @return saved {@link EventData}.
     */
    public EventData log(String configurationId, String eventType, String tag, JsonNode metaInfo) {
        EventData eventData = null;
        eventData = createEventData(configurationId, eventType, tag, metaInfo);
        eventDao.saveItem(eventData);
        return eventData;
    }

    private EventData createEventData(String configurationId, String eventType, String tag, JsonNode metaInfo) {
        EventData eventData = new EventData();
        eventData.setConfigurationId(configurationId);
        eventData.setEventType(eventType);
        eventData.setMetaInfo(metaInfo);
        eventData.setTag(tag);
        eventData.setId(UUID.randomUUID().toString());
        eventData.setCreatedDate(new Date());
        return eventData;
    }
}