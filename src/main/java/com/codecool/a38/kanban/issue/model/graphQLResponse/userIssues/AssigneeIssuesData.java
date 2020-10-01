package com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssigneeIssuesData {

	@JsonProperty("user")
	private UserWithMemberships user;
}