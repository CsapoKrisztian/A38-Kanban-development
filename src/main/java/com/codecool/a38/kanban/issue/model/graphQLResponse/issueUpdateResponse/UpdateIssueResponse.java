package com.codecool.a38.kanban.issue.model.graphQLResponse.issueUpdateResponse;

import com.google.gson.annotations.SerializedName;

public class UpdateIssueResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}