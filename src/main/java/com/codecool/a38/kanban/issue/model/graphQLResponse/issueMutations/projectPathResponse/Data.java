package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.projectPathResponse;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("issue")
    private Issue issue;

    public Issue getIssue() {
        return issue;
    }
}