package com.codecool.a38.kanban.issue.model;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Group;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project implements Comparable<Project> {

    private String id;

    private String fullPath;

    private String name;

    private Group group;

    @Override
    public int compareTo(Project project) {
        return this.getProjectDisplayName().compareTo(project.getProjectDisplayName());
    }

    private String getProjectDisplayName() {
        return group != null ? group.getName() + "/" + name : name;
    }
}
