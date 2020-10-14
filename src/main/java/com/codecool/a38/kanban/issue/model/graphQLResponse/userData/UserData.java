package com.codecool.a38.kanban.issue.model.graphQLResponse.userData;

import com.codecool.a38.kanban.issue.model.graphQLResponse.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserData {

    @JsonProperty("user")
    private User user;

}
