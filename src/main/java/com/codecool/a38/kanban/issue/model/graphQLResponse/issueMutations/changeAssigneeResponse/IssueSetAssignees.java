package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.changeAssigneeResponse;

import com.google.gson.annotations.SerializedName;

public class IssueSetAssignees {

    @SerializedName("issue")
    private Issue issue;

    public Issue getIssue() {
        return issue;
    }
}