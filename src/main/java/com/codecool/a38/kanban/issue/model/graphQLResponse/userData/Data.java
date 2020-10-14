package com.codecool.a38.kanban.issue.model.graphQLResponse.userData;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }
}