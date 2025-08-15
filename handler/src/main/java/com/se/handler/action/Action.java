package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;

import java.util.Map;

/**
 * Main interface for actions.
 *
 * @param <T> return type.
 */
public interface Action<T> {

    /**
     * Performs an action.
     *
     * @param body  the request body
     * @param query the request query parameters
     * @return the payload object
     */
    T perform(String body, Map<String, String> query);

    /**
     * Performs an action.
     *
     * @param request the aws request
     * @return the payload object
     */
    T perform(AwsProxyRequest request);

     /**
     * Performs an action.
     *
     * @param request the aws request
     * @return the payload object
     */
    T perform(AwsProxyRequest request, String string);

    /**
     * Returns code of action type.
     *
     * @return instance of {@link ActionType}.
     */
    default ActionType getActionType() {
        return ActionType.DEFAULT;
    }
}
