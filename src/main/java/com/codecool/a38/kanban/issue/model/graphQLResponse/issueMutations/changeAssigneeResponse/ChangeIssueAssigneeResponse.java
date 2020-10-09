package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.changeAssigneeResponse;

import com.google.gson.annotations.SerializedName;

public class ChangeIssueAssigneeResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}