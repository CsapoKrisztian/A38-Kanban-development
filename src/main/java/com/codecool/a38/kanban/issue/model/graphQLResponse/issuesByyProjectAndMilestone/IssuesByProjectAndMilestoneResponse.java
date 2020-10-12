package com.codecool.a38.kanban.issue.model.graphQLResponse.issuesByyProjectAndMilestone;

import com.google.gson.annotations.SerializedName;

public class IssuesByProjectAndMilestoneResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}