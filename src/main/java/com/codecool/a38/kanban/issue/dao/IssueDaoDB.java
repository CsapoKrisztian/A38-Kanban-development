package com.codecool.a38.kanban.issue.dao;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneesIssues;
import com.codecool.a38.kanban.issue.model.transfer.StoriesIssues;
import com.codecool.a38.kanban.issue.repository.IssueRepository;
import com.codecool.a38.kanban.issue.repository.MileStoneRepository;
import com.codecool.a38.kanban.issue.repository.ProjectRepository;
import com.codecool.a38.kanban.issue.repository.StoryRepository;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class IssueDaoDB implements IssueDao {

    private IssueRepository issueRepository;

    private ProjectRepository projectRepository;

    private MileStoneRepository mileStoneRepository;

    private StoryRepository storyRepository;

    @Override
    public List<Issue> getAll() {
        return issueRepository.findAll();
    }

    @Override
    public List<String> getStatuses() {
        return DataManager.getStatuses();
    }

    @Override
    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<MileStone> getMilestones() {
        return mileStoneRepository.findAll();
    }

    @Override
    public List<AssigneesIssues> getIssuesOrderedByAssignee() {
        Map<Assignee, List<Issue>> issuesOrderedByAssignees = new HashMap<>();
        issueRepository.findAll().forEach(issue -> {
            Status status = issue.getStatus();
            if (status != null) {
                Assignee assignee = issue.getAssignee();
                if (!issuesOrderedByAssignees.containsKey(assignee)) {
                    issuesOrderedByAssignees.put(assignee, new ArrayList<>());
                }
                issuesOrderedByAssignees.get(assignee).add(issue);
            }
        });
        return issuesOrderedByAssignees.entrySet().stream()
                .map(e -> AssigneesIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<StoriesIssues> getIssuesOrderedByStory() {
        Map<Story, List<Issue>> issuesOrderedByStory = new HashMap<>();
        issueRepository.findAll().forEach(issue -> {
            Story story = issue.getStory();
            Status status = issue.getStatus();
            if (story != null && status != null) {
                if (!issuesOrderedByStory.containsKey(story)) {
                    issuesOrderedByStory.put(story, new ArrayList<>());
                }
                issuesOrderedByStory.get(story).add(issue);
            }
        });
        return issuesOrderedByStory.entrySet().stream()
                .map(e -> StoriesIssues.builder()
                        .story(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Story> getStories() {
        return storyRepository.findAll();
    }

    @Override
    public void save(Issue issue) {
        log.info("Save issue: " + issue);
        issueRepository.save(issue);
    }

}
