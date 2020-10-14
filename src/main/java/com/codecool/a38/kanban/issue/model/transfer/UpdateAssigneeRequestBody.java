package com.codecool.a38.kanban.issue.model.transfer;

import lombok.Data;

@Data
public class UpdateAssigneeRequestBody {

    private String issueId;

    private String newAssigneeId;

}
