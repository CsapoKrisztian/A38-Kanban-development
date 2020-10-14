package com.codecool.a38.kanban.issue.model;

import lombok.Data;

@Data
public class UpdateIssueRequestBody {

    private int projectID;

    private String issueId;

    private String newStatusTitle;

}
