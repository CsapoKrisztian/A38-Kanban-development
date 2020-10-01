package com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AssigneeIssuesData {

	@JsonProperty("user")
	private UserWithMemberships user;
}