package com.codecool.a38.kanban.issue.model.graphQLResponse.issueSetAsignee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssueSetAssigneesData {

    @JsonProperty("issueSetAssignees")
    private IssueSetAssignees issueSetAssignees;

}
