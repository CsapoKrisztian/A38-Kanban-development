package com.codecool.a38.kanban.issue.model.graphQLResponse.projects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectsResponse{

	@JsonProperty("data")
	private ProjectsData data;
}