package com.codecool.a38.kanban.dao;

import com.codecool.a38.kanban.model.Issue;

import java.util.List;

public interface IssueDao {

    List<Issue> getAll();

    Issue save(Issue issue);

}
