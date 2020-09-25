package com.codecool.a38.kanban.issue.dao;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneesIssues;

import java.util.List;
import java.util.Map;

public interface IssueDao {

    List<Issue> getAll();

    void save(Issue build);

    List<String> getStatuses();

    List<Project> getProjects();

    List<MileStone> getMilestones();

    List<AssigneesIssues> getIssuesOrderedByAssignee();

    Map<String, List<Issue>> getIssuesOrderedByStory();

    List<Story> getStories();

}
