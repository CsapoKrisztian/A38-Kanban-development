package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectAllIssues;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("project")
    private Project project;

    public Project getProject() {
        return project;
    }
}