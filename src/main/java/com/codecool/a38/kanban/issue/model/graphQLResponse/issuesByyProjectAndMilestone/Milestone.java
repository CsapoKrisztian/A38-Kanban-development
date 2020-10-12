package com.codecool.a38.kanban.issue.model.graphQLResponse.issuesByyProjectAndMilestone;

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