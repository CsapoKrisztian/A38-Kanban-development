package com.codecool.a38.kanban.issue.model.graphQLResponse.issueUpdateResponse;

import com.google.gson.annotations.SerializedName;

public class UpdateIssue {

    @SerializedName("issue")
    private Issue issue;

    public Issue getIssue() {
        return issue;
    }
}