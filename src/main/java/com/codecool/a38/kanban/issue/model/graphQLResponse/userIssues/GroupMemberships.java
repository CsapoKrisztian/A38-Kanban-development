package com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GroupMemberships {

    @JsonProperty("nodes")
    private List<GroupMembershipNode> nodes;

}