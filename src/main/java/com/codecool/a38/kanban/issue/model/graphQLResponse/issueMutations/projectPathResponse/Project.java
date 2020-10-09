package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.projectPathResponse;

import com.google.gson.annotations.SerializedName;

public class Project {

    @SerializedName("fullPath")
    private String fullPath;

    public String getFullPath() {
        return fullPath;
    }
}