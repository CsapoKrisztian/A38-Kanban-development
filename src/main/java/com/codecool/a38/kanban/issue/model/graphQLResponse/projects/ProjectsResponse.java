package com.codecool.a38.kanban.issue.model.graphQLResponse.projects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectsResponse {

    @JsonProperty("data")
    private ProjectsData data;

}
