package com.codecool.a38.kanban.issue.model.graphQLResponse.issuesByyProjectAndMilestone;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Assignees {

    @SerializedName("nodes")
    private List<NodesItem> nodes;

    public List<NodesItem> getNodes() {
        return nodes;
    }
}