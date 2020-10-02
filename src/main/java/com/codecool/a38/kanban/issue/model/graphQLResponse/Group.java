package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Group {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}
