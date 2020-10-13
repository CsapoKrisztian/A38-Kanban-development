package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.changeAssigneeResponse;

import com.google.gson.annotations.SerializedName;

public class Issue {

    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }
}