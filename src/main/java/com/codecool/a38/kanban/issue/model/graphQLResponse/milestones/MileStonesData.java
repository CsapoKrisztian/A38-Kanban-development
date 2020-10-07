package com.codecool.a38.kanban.issue.model.graphQLResponse.milestones;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Projects;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MileStonesData {

    @JsonProperty("projects")
    private Projects projects;

}
