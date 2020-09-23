package com.codecool.a38.kanban.model.generated;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Assignees{

	@JsonProperty("nodes")
	private List<NodesItem> nodes;
}