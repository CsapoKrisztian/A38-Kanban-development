package com.codecool.a38.kanban.issue.model.graphQLResponse.stories;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Projects;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StoriesData {

	@JsonProperty("projects")
	private Projects projects;

}