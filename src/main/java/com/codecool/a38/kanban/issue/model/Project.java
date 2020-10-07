package com.codecool.a38.kanban.issue.model;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Group;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project {

    private String id;

    private String name;

    private Group group;

}
