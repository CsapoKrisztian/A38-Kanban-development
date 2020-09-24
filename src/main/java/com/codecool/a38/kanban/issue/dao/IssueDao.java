package com.codecool.a38.kanban.issue.dao;

import com.codecool.a38.kanban.issue.model.Issue;

import java.util.List;

public interface IssueDao {

    List<Issue> getAll();

    void save(Issue issue);

}
