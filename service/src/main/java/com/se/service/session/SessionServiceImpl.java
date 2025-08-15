package com.se.service.session;

import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.se.domain.session.Session;
import com.se.domain.session.SessionStatus;
import com.se.service.validator.Validator;
import com.se.utils.SEServiceConstants;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SessionServiceImpl implements SessionService {

    private static final Logger LOGGER = LogManager.getLogger(SessionServiceImpl.class);

    private static final String COOKIE = "cookie";
    public static final String SESSION_ID = "GENERIC-CONFIGURATOR-Session-Id";

    private static final String HTTP_ONLY_PARAM = "HttpOnly";
    private static final String PATH_PARAM = "Path";
    private static final String EXPIRES = "Expires";
    private static final String SECURE = "Secure";

    private static final String KEY_VALUE_SEPARATOR = "=";
    private static final String SPLITTER = ";";
    private static final String SLASH = "/";

    private static final int YEAR_EXPIRES = 5;
    private static final int DAY_EXPIRES = 2;

    /**
     * Constructor.
     *
     */
    @Inject
    public SessionServiceImpl() {
    }

    @Override
    public Session createSession(Map<String, String> headers) {
        LOGGER.debug("Attempt to get session identifier. There the following header - [{}]", headers);
        Map<String, String> headerLowerCase = Optional.ofNullable(headers)
                .map(Map::entrySet)
                .map(Collection::stream)
                .map(stream -> stream.collect(Collectors.toMap(entry -> entry.getKey().toLowerCase(),
                        Map.Entry::getValue)))
                .orElse(Maps.newHashMap());
        Optional<String> applicationSessionId = Optional.of(headerLowerCase)
                .map(entry -> entry.get(COOKIE))
                .map(cookie -> Splitter.on(SPLITTER).omitEmptyStrings().trimResults().splitToList(cookie))
                .map(cookieList -> cookieList.stream()
                        .map(value -> value.split(KEY_VALUE_SEPARATOR))
                        .collect(Collectors.toMap(cookie -> cookie[0], cookie -> {
                            try {
                                return cookie[1];
                            } catch (ArrayIndexOutOfBoundsException ae) {
                                return cookie[0];
                            }
                        },
                                (cookie1, cookie2) -> cookie1)))
                .map(map -> map.get(SESSION_ID));
        if (applicationSessionId.isPresent()) {
            String sessionId = applicationSessionId.get();
            Validator.validateSessionId(sessionId);
            LOGGER.debug("GENERIC-CONFIGURATOR-Session-Id already exists - [{}]", sessionId);
            String cookie = buildCookie(sessionId);
            LOGGER.debug("Created the following cookies - [{}]", cookie);
            return new Session(cookie, SessionStatus.EXISTING, sessionId);
        } else {
            String sessionId = UUID.randomUUID().toString();
            LOGGER.debug("Creating a new GENERIC-CONFIGURATOR-Session-Id - [{}]", sessionId);
            String cookie = buildCookie(sessionId);
            LOGGER.debug("Created the following cookies - [{}]", cookie);
            return new Session(cookie, SessionStatus.NEW, sessionId);
        }
    }

    private String buildCookie(String sessionId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SEServiceConstants.DATE_TIME_FORMAT)
            .withLocale(Locale.UK);
        String expires = ZonedDateTime.now(ZoneId.of(SEServiceConstants.GMT_TIMEZONE))
                .plusYears(YEAR_EXPIRES)
                .plusDays(DAY_EXPIRES)
                .format(formatter);
        return new StringBuilder()
                .append(SESSION_ID)
                .append(KEY_VALUE_SEPARATOR)
                .append(sessionId)
                .append(SPLITTER)
                .append(EXPIRES)
                .append(KEY_VALUE_SEPARATOR)
                .append(expires)
                .append(SPLITTER)
                .append(HTTP_ONLY_PARAM)
                .append(SPLITTER)
                .append(PATH_PARAM)
                .append(KEY_VALUE_SEPARATOR)
                .append(SLASH)
                .append(SPLITTER)
                .append(SECURE)
                .append(SPLITTER)
                .toString();
    }
    
}
