package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueCurrentStatus;

import com.google.gson.annotations.SerializedName;

public class NodesItem {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}