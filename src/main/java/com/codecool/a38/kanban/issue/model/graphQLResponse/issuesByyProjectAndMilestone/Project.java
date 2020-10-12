package com.codecool.a38.kanban.issue.model.graphQLResponse.issuesByyProjectAndMilestone;

import com.google.gson.annotations.SerializedName;

public class Project {

    @SerializedName("issues")
    private Issues issues;

    public Issues getIssues() {
        return issues;
    }
}