package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issuesIID;

import com.google.gson.annotations.SerializedName;

public class Issue {

    @SerializedName("iid")
    private String iid;

    public String getIid() {
        return iid;
    }
}