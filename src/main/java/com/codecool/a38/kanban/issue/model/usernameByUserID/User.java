package com.codecool.a38.kanban.issue.model.usernameByUserID;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private String username;

    public String getUsername() {
        return username;
    }
}