package com.codecool.a38.kanban.issue.model.graphQLResponse.updateIssueData;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class UpdateIssueDataResponse {

    @JsonProperty("data")
    private UpdateIssueData data;

}
