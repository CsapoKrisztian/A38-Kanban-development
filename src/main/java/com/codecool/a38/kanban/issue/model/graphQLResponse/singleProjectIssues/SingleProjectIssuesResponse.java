package com.codecool.a38.kanban.issue.model.graphQLResponse.singleProjectIssues;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class SingleProjectIssuesResponse {

	@JsonProperty("data")
	private SingleProjectIssuesData data;

}