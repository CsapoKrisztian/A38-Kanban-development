package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Data {

    @JsonProperty("projects")
    private Projects projects;
}