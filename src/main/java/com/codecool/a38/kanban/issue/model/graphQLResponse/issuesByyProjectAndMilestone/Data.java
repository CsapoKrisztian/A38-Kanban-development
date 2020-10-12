package com.codecool.a38.kanban.issue.model.graphQLResponse.issuesByyProjectAndMilestone;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("project")
    private Project project;

    public Project getProject() {
        return project;
    }
}