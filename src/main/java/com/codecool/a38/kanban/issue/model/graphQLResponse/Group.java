package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Group {

    @JsonProperty("id")
    private String id;

    @JsonProperty("fullPath")
    private String fullPath;

    @JsonProperty("name")
    private String name;

    @JsonProperty("milestones")
    private Milestones milestones;

}
