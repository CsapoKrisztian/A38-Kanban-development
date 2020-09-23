package com.codecool.a38.kanban.repository;

import com.codecool.a38.kanban.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, String> {

}
