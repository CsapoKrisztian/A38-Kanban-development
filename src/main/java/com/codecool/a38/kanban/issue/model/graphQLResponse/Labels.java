package com.codecool.a38.kanban.issue.model.graphQLResponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Labels {

    @JsonProperty("nodes")
    private List<Label> nodes;

    @JsonProperty("pageInfo")
    private PageInfo pageInfo;

}