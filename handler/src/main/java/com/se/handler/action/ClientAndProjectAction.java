package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.util.json.Jackson;
import com.se.common.ClientAndProject;
import com.se.common.GlobalConstants;
import com.se.domain.holder.MainConfigHolder;
import com.se.service.httpClient.HttpClient;

import java.util.List;
import java.util.Map;

/**
 * The implementation of {@link Action} for reading templates by list of
 * commercial references.
 */
public class ClientAndProjectAction extends AbstractAction<List<ClientAndProject>> {

    private HttpClient httpClient;

    /**
     * Parametrized constructor.
     *
     * @param configurationService the specify service instance.
     */
    public ClientAndProjectAction(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ClientAndProject> perform(AwsProxyRequest request) {
        Map<String, Object> payload = Jackson.fromJsonString(request.getBody(),
                Map.class);
        String locale = (String) payload.get(GlobalConstants.LOCALE);
        String main = (String) payload.get(GlobalConstants.MAIN);
        String mainTableContent = httpClient.getMainTableContent(main, locale);

        MainConfigHolder mainConfigHolder = Jackson.fromJsonString(mainTableContent,
                MainConfigHolder.class);
        return mainConfigHolder.getClientAndProjects();

    }

    @Override
    public List<ClientAndProject> perform(String body, Map<String, String> query) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }

    @Override
    public List<ClientAndProject> perform(AwsProxyRequest request, String s) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
