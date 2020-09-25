package com.codecool.a38.kanban.issue.repository;

import com.codecool.a38.kanban.issue.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, String> {
}
