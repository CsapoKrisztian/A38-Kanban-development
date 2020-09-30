package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@lombok.Data
@Builder
public class Milestone {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;
}