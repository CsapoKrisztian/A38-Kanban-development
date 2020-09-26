package com.codecool.a38.kanban.issue.repository;

import com.codecool.a38.kanban.issue.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}
