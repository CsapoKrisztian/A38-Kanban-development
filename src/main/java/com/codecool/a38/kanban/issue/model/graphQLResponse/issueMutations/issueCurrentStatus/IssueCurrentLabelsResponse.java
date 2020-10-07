package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueCurrentStatus;

import com.google.gson.annotations.SerializedName;

public class IssueCurrentLabelsResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}