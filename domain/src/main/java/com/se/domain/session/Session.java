package com.se.domain.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * It represent a session object in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private String cookie;
    private String sessionId;
    private SessionStatus sessionStatus;

    /**
     * Copy constructor.
     *
     * @param session {@link Session} instance to copy.
     */
    public Session(Session session) {
        this.sessionStatus = session.sessionStatus;
        this.cookie = StringUtils.EMPTY;
        this.sessionId = session.sessionId;
    }

    public Session(String cookie, SessionStatus sessionStatus, String sessionId) {
        this.sessionStatus = sessionStatus;
        this.cookie = cookie;
        this.sessionId = sessionId;
    }
}
