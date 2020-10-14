package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DesignCollection {

    @JsonProperty("project")
    private ProjectNode project;

}
