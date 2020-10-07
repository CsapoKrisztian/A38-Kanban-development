package com.codecool.a38.kanban.issue.model.graphQLResponse.stories;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StoriesResponse{

	@JsonProperty("data")
	private StoriesData data;
}