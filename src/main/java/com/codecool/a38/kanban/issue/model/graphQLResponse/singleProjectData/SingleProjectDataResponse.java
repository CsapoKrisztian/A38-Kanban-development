package com.codecool.a38.kanban.issue.model.graphQLResponse.singleProjectData;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class SingleProjectDataResponse {

	@JsonProperty("data")
	private SingleProjectData data;

}