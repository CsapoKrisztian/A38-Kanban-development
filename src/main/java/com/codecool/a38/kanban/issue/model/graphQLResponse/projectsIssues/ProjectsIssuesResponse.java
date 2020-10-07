package com.codecool.a38.kanban.issue.model.graphQLResponse.projectsIssues;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProjectsIssuesResponse {

	@JsonProperty("data")
	private ProjectsIssuesData data;

}
