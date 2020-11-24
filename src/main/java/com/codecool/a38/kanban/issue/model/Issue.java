package com.codecool.a38.kanban.issue.model;

import com.codecool.a38.kanban.issue.model.graphQLResponse.User;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Label;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Milestone;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue implements Comparable<Issue> {

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

    @Override
    public int compareTo(Issue otherIssue) {
        if (this.priority == null && otherIssue.priority == null) return 0;
        if (this.priority == null) return 1;
        if (otherIssue.priority == null) return -1;
        return this.priority.getPriorityNum() - otherIssue.priority.getPriorityNum();
    }
}
