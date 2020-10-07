package com.codecool.a38.kanban.issue.model.graphQLResponse.projectsIssues;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Projects;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProjectsIssuesData {

    @JsonProperty("projects")
    private Projects projects;

}
