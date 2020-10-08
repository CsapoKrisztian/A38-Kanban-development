package com.codecool.a38.kanban.issue.model.graphQLResponse.projectsData;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Projects;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectsData {

	@JsonProperty("projects")
	private Projects projects;

}
