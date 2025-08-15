package com.se.common.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.se.common.ClientAndProject;
import com.se.common.holder.ClientAndProjectHolder;

public class ClientAndProjectDeserializer extends AbstractConfigParamTableDeserializer<ClientAndProjectHolder> {

    private static final String TABLE_NAME_PATTERN = "ClientAndProject";

    private static final String TITLE = "Title";
    private static final String UI_COMPONENT = "UI component";
    private static final String RANK = "Rank";
    private static final String TYPE = "Type";
    private static final String FORMAT = "Format";
    private static final String MANDATORY = "Mandatory";
    private static final String CRITERIA = "Criteria";

    @Override
    public String getConfigTableName() {
        return TABLE_NAME_PATTERN;
    }

    @Override
    public ClientAndProjectHolder deserializeFromParams(List<Map<String, String>> params) {
        ClientAndProjectHolder holder = new ClientAndProjectHolder();

        List<ClientAndProject> clientAndProjects = new ArrayList<>();
        for (Map<String, String> param : params) {
            ClientAndProject clientAndProject = new ClientAndProject();
            clientAndProject.setTitle(param.remove(TITLE));
            clientAndProject.setUiComponent(param.remove(UI_COMPONENT));
            clientAndProject.setRank(this.getIntFromParam(param.remove(RANK)));
            clientAndProject.setType(param.remove(TYPE));
            clientAndProject.setFormat(param.remove(FORMAT));
            clientAndProject.setCriteria(param.remove(CRITERIA));
            clientAndProject.setMandatory(param.get(MANDATORY) != null ? Boolean.valueOf(param.get(MANDATORY)) : null);
            clientAndProjects.add(clientAndProject);
        }
        holder.setClientAndProjects(clientAndProjects);

        return holder;
    }

}
