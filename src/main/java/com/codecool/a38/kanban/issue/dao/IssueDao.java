package com.codecool.a38.kanban.issue.dao;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneesIssues;
import com.codecool.a38.kanban.issue.model.transfer.StoriesIssues;

import java.util.List;

public interface IssueDao {

    List<Issue> getAll();

    void save(Issue build);

    List<String> getStatuses();

    List<Project> getProjects();

    List<MileStone> getMilestones();

    List<AssigneesIssues> getIssuesOrderedByAssignee();

    List<StoriesIssues> getIssuesOrderedByStory();

    List<Story> getStories();

}
