package com.codecool.a38.kanban.model.generated;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Milestone{

	@JsonProperty("id")
	private String id;

	@JsonProperty("title")
	private String title;
}