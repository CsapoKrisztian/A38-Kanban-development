package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProjectsDataResponse {

	@JsonProperty("data")
	private Data data;
}