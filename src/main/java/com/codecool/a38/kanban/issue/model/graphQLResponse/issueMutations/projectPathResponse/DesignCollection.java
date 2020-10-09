package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.projectPathResponse;

import com.google.gson.annotations.SerializedName;

public class DesignCollection {

    @SerializedName("project")
    private Project project;

    public Project getProject() {
        return project;
    }
}