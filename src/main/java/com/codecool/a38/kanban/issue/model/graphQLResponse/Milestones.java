package com.codecool.a38.kanban.issue.model.graphQLResponse;

import java.util.List;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Milestone;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Milestones{

	@JsonProperty("nodes")
	private List<Milestone> nodes;

}