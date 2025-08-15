package com.se.service.session;

import java.util.Map;

import com.se.domain.session.Session;

public interface SessionService {
    
    /**
     * Create session.
     *
     * @param headers the map with headers.
     * @return session object
     */
    Session createSession(Map<String, String> headers);

}
