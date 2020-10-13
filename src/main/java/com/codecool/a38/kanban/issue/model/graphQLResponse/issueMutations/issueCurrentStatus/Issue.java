package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueCurrentStatus;

import com.google.gson.annotations.SerializedName;

public class Issue {

    @SerializedName("labels")
    private Labels labels;

    public Labels getLabels() {
        return labels;
    }
}