package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectAllIssues;

import com.google.gson.annotations.SerializedName;

public class Milestone {

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