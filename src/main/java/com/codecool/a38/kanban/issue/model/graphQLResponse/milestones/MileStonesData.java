package com.codecool.a38.kanban.issue.model.graphQLResponse.milestones;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Projects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MileStonesData {

    @JsonProperty("projects")
    private Projects projects;

}