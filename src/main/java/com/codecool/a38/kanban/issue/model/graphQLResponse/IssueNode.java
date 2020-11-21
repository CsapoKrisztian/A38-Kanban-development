package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueNode {

    @JsonProperty("id")
    private String id;

    @JsonProperty("iid")
    private String iid;

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
    private String reference;

    @JsonProperty("assignees")
    private Assignees assignees;

    @JsonProperty("milestone")
    private Milestone milestone;

    @JsonProperty("labels")
    private Labels labels;

}
