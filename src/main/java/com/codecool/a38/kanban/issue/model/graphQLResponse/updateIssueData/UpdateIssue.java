package com.codecool.a38.kanban.issue.model.graphQLResponse.updateIssueData;

import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateIssue {

    @JsonProperty("issue")
    private IssueNode issue;


}
