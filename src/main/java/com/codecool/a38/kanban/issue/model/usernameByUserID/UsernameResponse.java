package com.codecool.a38.kanban.issue.model.usernameByUserID;

import com.google.gson.annotations.SerializedName;

public class UsernameResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}