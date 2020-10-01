package com.codecool.a38.kanban.issue.model.transfer;

import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Label;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Milestone;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProjectsData {

    private List<AssigneesIssues> assigneesIssuesList;

    private List<StoriesIssues> storiesIssuesList;

    private Set<Project> projects = new HashSet<>();

    private Set<Milestone> mileStones = new HashSet<>();

    private Set<Label> stories = new HashSet<>();


    public void addProject(Project project) {
        projects.add(project);
    }

    public void addMileStone(Milestone milestone) {
        mileStones.add(milestone);
    }

    public void addStory(Label story) {
        stories.add(story);
    }

}
