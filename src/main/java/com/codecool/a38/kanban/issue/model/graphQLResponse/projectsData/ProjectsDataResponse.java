package com.codecool.a38.kanban.issue.model.graphQLResponse.projectsData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectsDataResponse {

    @JsonProperty("data")
    private ProjectsData data;

}
