package com.codecool.a38.kanban.issue.model.graphQLResponse.milestones;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MilestonesResponse{

	@JsonProperty("data")
	private MileStonesData data;
}