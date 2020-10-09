package com.codecool.a38.kanban.issue.model.graphQLResponse.singleProjectData;

import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class SingleProjectData {

    @JsonProperty("project")
    private ProjectNode project;

}