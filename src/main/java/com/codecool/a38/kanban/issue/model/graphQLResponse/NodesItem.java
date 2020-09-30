package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class NodesItem {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;


    @JsonProperty("title")
    private String title;

    @JsonProperty("labels")
    private Labels labels;

    @JsonProperty("color")
    private String color;



    @JsonProperty("reference")
    private String reference;

}
