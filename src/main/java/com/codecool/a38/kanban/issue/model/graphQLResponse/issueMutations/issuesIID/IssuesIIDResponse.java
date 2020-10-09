package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issuesIID;

import com.google.gson.annotations.SerializedName;

public class IssuesIIDResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}