package com.codecool.a38.kanban.issue.model.graphQLResponse.milestones;

import java.util.List;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Milestone;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Milestones{

	@JsonProperty("nodes")
	private List<Milestone> nodes;
}