package com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues;

import com.codecool.a38.kanban.issue.model.graphQLResponse.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserWithMemberships extends User {

    @JsonProperty("groupMemberships")
    private GroupMemberships groupMemberships;

    @JsonProperty("projectMemberships")
    private ProjectMemberships projectMemberships;

}