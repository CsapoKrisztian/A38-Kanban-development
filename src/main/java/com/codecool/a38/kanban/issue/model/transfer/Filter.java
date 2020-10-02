package com.codecool.a38.kanban.issue.model.transfer;

import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Label;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Milestone;
import lombok.Data;

import java.util.List;

@Data
public class Filter {

    private List<Project> projects;

    private List<Milestone> milestones;

    private List<Label> stories;

}
