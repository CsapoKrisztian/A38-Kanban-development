package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.statusIDresponse;

import com.google.gson.annotations.SerializedName;

public class StatusIDResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}