package com.codecool.a38.kanban.issue.model.graphQLResponse.issueUpdateResponse;

import com.google.gson.annotations.SerializedName;

public class Issue {

    @SerializedName("labels")
    private Labels labels;

    public Labels getLabels() {
        return labels;
    }
}