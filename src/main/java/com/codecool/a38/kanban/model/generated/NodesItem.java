package com.codecool.a38.kanban.model.generated;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodesItem{

	@JsonProperty("fullPath")
	private String fullPath;

	@JsonProperty("webUrl")
	private String webUrl;

	@JsonProperty("avatarUrl")
	private String avatarUrl;

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private String id;

	@JsonProperty("issues")
	private Issues issues;

	@JsonProperty("milestone")
	private Milestone milestone;

	@JsonProperty("description")
	private String description;

	@JsonProperty("assignees")
	private Assignees assignees;

	@JsonProperty("title")
	private String title;

	@JsonProperty("designCollection")
	private DesignCollection designCollection;

	@JsonProperty("labels")
	private Labels labels;
}