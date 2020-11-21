package com.codecool.a38.kanban.issue.model.transfer;

import lombok.Data;

@Data
public class UpdateStatusRequestBody {

    private String issueId;

    private String newStatusTitle;

    private String projectFullPath;

}
