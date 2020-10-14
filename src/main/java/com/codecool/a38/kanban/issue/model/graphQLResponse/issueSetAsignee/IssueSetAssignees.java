package com.codecool.a38.kanban.issue.model.graphQLResponse.issueSetAsignee;

import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class IssueSetAssignees {

    @SerializedName("issue")
    private IssueNode issue;

}
