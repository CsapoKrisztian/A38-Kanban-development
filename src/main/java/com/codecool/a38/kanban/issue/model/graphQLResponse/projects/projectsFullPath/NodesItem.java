package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectsFullPath;

import com.google.gson.annotations.SerializedName;

public class NodesItem {

    @SerializedName("fullPath")
    private String fullPath;

    public String getFullPath() {
        return fullPath;
    }
}