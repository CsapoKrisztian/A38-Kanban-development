package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.statusIDresponse;

import com.google.gson.annotations.SerializedName;

public class Project {

    @SerializedName("label")
    private Label label;

    public Label getLabel() {
        return label;
    }
}