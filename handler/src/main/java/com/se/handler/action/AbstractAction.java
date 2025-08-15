package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;

/**
 * Abstract action implementation that every action should extend.
 *
 * @param <T> return type.
 */
public abstract class AbstractAction<T> implements Action<T> {

    @Override
    public T perform(AwsProxyRequest request) {
        return perform(request.getBody(), request.getQueryStringParameters());
    }
}
