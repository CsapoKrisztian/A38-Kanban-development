package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectAllIssues;

import com.google.gson.annotations.SerializedName;

public class AllIssuesByProjectResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}