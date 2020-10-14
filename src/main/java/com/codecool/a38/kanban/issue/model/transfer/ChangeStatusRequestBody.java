package com.codecool.a38.kanban.issue.model.transfer;

import lombok.Data;

@Data
public class ChangeStatusRequestBody {

    private String issueId;

    private String newStatusTitle;

}
