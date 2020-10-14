package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectNode {

    @JsonProperty("id")
    private String id;

    @JsonProperty("fullPath")
    private String fullPath;

    @JsonProperty("name")
    private String name;

    @JsonProperty("group")
    private Group group;

    @JsonProperty("issues")
    private Issues issues;

    @JsonProperty("milestones")
    private Milestones milestones;

    @JsonProperty("labels")
    private Labels labels;

    @JsonProperty("label")
    private Label label;

}
