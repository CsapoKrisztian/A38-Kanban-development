package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueUpdateResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Labels {

    @SerializedName("nodes")
    private List<NodesItem> nodes;

    public List<NodesItem> getNodes() {
        return nodes;
    }
}