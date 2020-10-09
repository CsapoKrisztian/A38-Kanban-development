package com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectAllIssues;

import com.google.gson.annotations.SerializedName;

public class NodesItem {

    @SerializedName("reference")
    private String reference;

    @SerializedName("milestone")
    private Milestone milestone;

    @SerializedName("webUrl")
    private String webUrl;

    @SerializedName("dueDate")
    private String dueDate;

    @SerializedName("description")
    private String description;

    @SerializedName("assignees")
    private Assignees assignees;

    @SerializedName("id")
    private String id;

    @SerializedName("userNotesCount")
    private int userNotesCount;

    @SerializedName("title")
    private String title;

    @SerializedName("labels")
    private Labels labels;

    @SerializedName("color")
    private String color;

    public String getReference() {
        return reference;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }

    public Assignees getAssignees() {
        return assignees;
    }

    public String getId() {
        return id;
    }

    public int getUserNotesCount() {
        return userNotesCount;
    }

    public String getTitle() {
        return title;
    }

    public Labels getLabels() {
        return labels;
    }

    public String getColor() {
        return color;
    }
}