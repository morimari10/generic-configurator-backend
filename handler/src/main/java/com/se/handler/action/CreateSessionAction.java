package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.se.domain.session.Session;
import com.se.service.session.SessionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * The implementation of {@link Action} for reading templates by list of commercial references.
 */
public class CreateSessionAction extends AbstractAction<Session> {

    private static final Logger LOGGER = LogManager.getLogger(CreateSessionAction.class);

    private SessionService sessionService;

    /**
     * Parametrized constructor.
     *
     * @param sessionService the template service instance.
     */
    public CreateSessionAction(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Session perform(AwsProxyRequest request) {
        LOGGER.info("Attempt to create a session");
        return sessionService.createSession(request.getHeaders());
    }

    @Override
    public Session perform(String body, Map<String, String> query) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }

    @Override
    public ActionType getActionType() {
        return ActionType.SESSION;
    }

    @Override
    public Session perform(AwsProxyRequest request, String s) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
