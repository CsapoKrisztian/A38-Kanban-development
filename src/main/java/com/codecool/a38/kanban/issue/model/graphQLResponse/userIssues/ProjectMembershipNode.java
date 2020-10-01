package com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues;

import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectMembershipNode {

    @JsonProperty("project")
    private ProjectNode project;

}
