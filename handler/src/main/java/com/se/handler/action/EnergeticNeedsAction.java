package com.se.handler.action;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.util.json.Jackson;
import com.se.common.EnergeticNeeds;
import com.se.common.GlobalConstants;
import com.se.domain.holder.MainConfigHolder;
import com.se.service.httpClient.HttpClient;

import java.util.List;
import java.util.Map;

/**
 * The implementation of {@link Action} for reading templates by list of
 * commercial references.
 */
public class EnergeticNeedsAction extends AbstractAction<List<EnergeticNeeds>> {

    private HttpClient httpClient;

    /**
     * Parametrized constructor.
     *
     * @param configurationService the specify service instance.
     */
    public EnergeticNeedsAction(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EnergeticNeeds> perform(AwsProxyRequest request) {
        Map<String, Object> payload = Jackson.fromJsonString(request.getBody(),
                Map.class);
        String locale = (String) payload.get(GlobalConstants.LOCALE);
        String main = (String) payload.get(GlobalConstants.MAIN);
        String mainTableContent = httpClient.getMainTableContent(main, locale);

        MainConfigHolder mainConfigHolder = Jackson.fromJsonString(mainTableContent,
                MainConfigHolder.class);
        return mainConfigHolder.getEnergeticNeeds();

    }

    @Override
    public List<EnergeticNeeds> perform(String body, Map<String, String> query) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }

    @Override
    public List<EnergeticNeeds> perform(AwsProxyRequest request, String s) {
        throw new UnsupportedOperationException("Unimplemented method 'perform'");
    }
}
