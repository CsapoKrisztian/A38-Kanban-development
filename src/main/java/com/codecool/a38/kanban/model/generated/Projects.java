package com.codecool.a38.kanban.model.generated;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Projects{

	@JsonProperty("nodes")
	private List<NodesItem> nodes;
}