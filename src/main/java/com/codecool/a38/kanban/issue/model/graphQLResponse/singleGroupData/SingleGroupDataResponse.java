package com.codecool.a38.kanban.issue.model.graphQLResponse.singleGroupData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SingleGroupDataResponse {

    @JsonProperty("data")
    private SingleGroupData data;

}
