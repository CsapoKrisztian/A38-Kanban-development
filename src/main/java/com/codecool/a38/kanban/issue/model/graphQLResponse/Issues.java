package com.codecool.a38.kanban.issue.model.graphQLResponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Issues {

    @JsonProperty("nodes")
    private List<IssueNode> nodes;

    @JsonProperty("pageInfo")
    private PageInfo pageInfo;

}
