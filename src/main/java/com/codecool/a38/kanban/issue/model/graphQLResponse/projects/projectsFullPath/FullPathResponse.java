package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectsFullPath;

import com.google.gson.annotations.SerializedName;

public class FullPathResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}