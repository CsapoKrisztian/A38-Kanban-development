package com.codecool.a38.kanban.issue.model;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Assignee;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Milestone;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue {

    private String id;

    private String title;

    private String description;

    private String webUrl;

    private String dueDate;

    private Integer userNotesCount;

    private String reference;


    private Assignee assignee;

    private Milestone mileStone;


    private Story story;

    private Status status;

    private Priority priority;


    private Project project;

}
