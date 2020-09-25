package com.codecool.a38.kanban.issue.model.generated;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class NodesItem {

    @JsonProperty("webUrl")
    private String webUrl;

    @JsonProperty("avatarUrl")
    private String avatarUrl;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("dueDate")
    private String dueDate;

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

    @JsonProperty("labels")
    private Labels labels;

    @JsonProperty("color")
    private String color;

    @JsonProperty("userNotesCount")
    private Integer userNotesCount;

    @JsonProperty("reference")
    private String reference;

}
