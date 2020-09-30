package com.codecool.a38.kanban.issue.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project {

    private String id;

    private String name;

}
