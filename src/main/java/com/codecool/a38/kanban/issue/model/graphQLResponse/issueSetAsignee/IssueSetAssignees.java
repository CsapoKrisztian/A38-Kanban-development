package com.codecool.a38.kanban.issue.model.graphQLResponse.issueSetAsignee;

import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssueSetAssignees {

    @JsonProperty("issue")
    private IssueNode issue;

}
