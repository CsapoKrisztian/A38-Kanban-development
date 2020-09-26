package com.codecool.a38.kanban.issue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Issue {

    @Id
    private String issueId;

    private String title;

    private String description;

    private String issueUrl;

    private String dueDate;

    private Integer userNotesCount;

    private String reference;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Priority priority;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Status status;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Story story;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Project project;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private MileStone mileStone;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Assignee assignee;

}
