package com.codecool.a38.kanban.issue.model.graphQLResponse.issueSetAsignee;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class IssueSetAssigneesData {

    @SerializedName("issueSetAssignees")
    private IssueSetAssignees issueSetAssignees;

}
