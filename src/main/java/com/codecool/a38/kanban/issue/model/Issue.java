package com.codecool.a38.kanban.issue.model;

import com.codecool.a38.kanban.issue.model.graphQLResponse.User;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Label;
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


    private User assignee;

    private Milestone mileStone;


    private Label status;

    private Label story;

    private Label priority;


    private Project project;

}
