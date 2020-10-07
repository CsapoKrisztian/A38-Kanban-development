package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.projectPathResponse;

import com.google.gson.annotations.SerializedName;

public class ProjectPathResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}