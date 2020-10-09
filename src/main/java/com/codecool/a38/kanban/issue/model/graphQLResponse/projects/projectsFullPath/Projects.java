package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectsFullPath;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Projects {

    @SerializedName("nodes")
    private List<NodesItem> nodes;

    public List<NodesItem> getNodes() {
        return nodes;
    }
}