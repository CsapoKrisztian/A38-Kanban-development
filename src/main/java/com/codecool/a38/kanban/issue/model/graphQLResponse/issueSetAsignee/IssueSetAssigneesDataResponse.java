package com.codecool.a38.kanban.issue.model.graphQLResponse.issueSetAsignee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssueSetAssigneesDataResponse {

    @JsonProperty("data")
    private IssueSetAssigneesData data;

}
