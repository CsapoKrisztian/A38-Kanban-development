package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectAllIssues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Assignees {

    @SerializedName("nodes")
    private List<Object> nodes;

    public List<Object> getNodes() {
        return nodes;
    }
}