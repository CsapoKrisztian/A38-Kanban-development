package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueNode {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("webUrl")
    private String webUrl;

    @JsonProperty("dueDate")
    private String dueDate;

    @JsonProperty("userNotesCount")
    private Integer userNotesCount;

    @JsonProperty("reference")
    private Integer reference;

    @JsonProperty("assignees")
    private Assignees assignees;

    @JsonProperty("milestone")
    private Milestone milestone;


}
