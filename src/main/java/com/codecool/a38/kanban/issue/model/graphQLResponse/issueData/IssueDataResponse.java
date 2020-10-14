package com.codecool.a38.kanban.issue.model.graphQLResponse.issueData;

import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssueDataResponse {

    @JsonProperty("data")
    private IssueNode data;

}
