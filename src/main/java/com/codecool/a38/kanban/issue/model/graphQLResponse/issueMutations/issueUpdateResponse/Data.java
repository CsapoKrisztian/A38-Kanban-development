package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueUpdateResponse;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("updateIssue")
    private UpdateIssue updateIssue;

    public UpdateIssue getUpdateIssue() {
        return updateIssue;
    }
}