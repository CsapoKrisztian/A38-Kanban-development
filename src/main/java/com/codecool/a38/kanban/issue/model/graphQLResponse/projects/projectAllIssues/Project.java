package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectAllIssues;

import com.google.gson.annotations.SerializedName;

public class Project {

    @SerializedName("issues")
    private Issues issues;

    public Issues getIssues() {
        return issues;
    }
}