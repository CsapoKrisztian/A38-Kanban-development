package com.codecool.a38.kanban.issue.model.graphQLResponse.singleProjectIssues;

import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Projects;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class SingleProjectIssuesData {

    @JsonProperty("project")
    private ProjectNode project;

}