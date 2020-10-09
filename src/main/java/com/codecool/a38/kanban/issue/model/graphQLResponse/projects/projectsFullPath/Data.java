package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectsFullPath;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("projects")
    private Projects projects;

    public Projects getProjects() {
        return projects;
    }
}