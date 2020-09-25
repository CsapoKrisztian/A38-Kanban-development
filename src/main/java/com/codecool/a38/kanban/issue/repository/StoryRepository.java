package com.codecool.a38.kanban.issue.repository;

import com.codecool.a38.kanban.issue.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, String> {
}
