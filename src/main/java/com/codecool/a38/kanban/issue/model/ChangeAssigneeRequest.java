package com.codecool.a38.kanban.issue.model;

import lombok.Data;

@Data
public class ChangeAssigneeRequest {

    private String assignee;
    private String issueID;
}
