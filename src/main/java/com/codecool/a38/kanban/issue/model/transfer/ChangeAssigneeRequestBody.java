package com.codecool.a38.kanban.issue.model.transfer;

import lombok.Data;

@Data
public class ChangeAssigneeRequestBody {

    private String assignee;
    private String issueID;
}