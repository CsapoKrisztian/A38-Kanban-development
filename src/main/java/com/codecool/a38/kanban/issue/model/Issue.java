package com.codecool.a38.kanban.issue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Issue {

    private String issueId;

    private String title;

    private String description;

    private String issueUrl;

    private String dueDate;

    private Integer userNotesCount;

    private String reference;

    private Priority priority;

    private Status status;

    private Story story;

    private Project project;

    private MileStone mileStone;

    private Assignee assignee;

}
