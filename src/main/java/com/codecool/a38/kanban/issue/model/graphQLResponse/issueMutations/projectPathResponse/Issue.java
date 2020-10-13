package com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.projectPathResponse;

import com.google.gson.annotations.SerializedName;

public class Issue {

    @SerializedName("iid")
    private String iid;

    @SerializedName("designCollection")
    private DesignCollection designCollection;

    public String getIid() {
        return iid;
    }

    public DesignCollection getDesignCollection() {
        return designCollection;
    }
}