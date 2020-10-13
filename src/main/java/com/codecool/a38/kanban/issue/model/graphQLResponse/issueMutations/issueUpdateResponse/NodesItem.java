package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueUpdateResponse;

import com.google.gson.annotations.SerializedName;

public class NodesItem {

    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }
}