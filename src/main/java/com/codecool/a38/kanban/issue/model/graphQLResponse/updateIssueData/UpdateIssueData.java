package com.codecool.a38.kanban.issue.model.graphQLResponse.updateIssueData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateIssueData {

    @JsonProperty("updateIssue")
    private UpdateIssue updateIssue;

}
