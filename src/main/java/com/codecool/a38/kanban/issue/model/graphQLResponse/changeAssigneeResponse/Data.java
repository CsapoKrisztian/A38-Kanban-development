package com.codecool.a38.kanban.issue.model.graphQLResponse.changeAssigneeResponse;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("issueSetAssignees")
    private IssueSetAssignees issueSetAssignees;

    public IssueSetAssignees getIssueSetAssignees() {
        return issueSetAssignees;
    }
}