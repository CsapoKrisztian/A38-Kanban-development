package com.codecool.a38.kanban.issue.model.graphQLResponse.userData;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class UserDataResponse {

    @JsonProperty("data")
    private UserData data;

}
