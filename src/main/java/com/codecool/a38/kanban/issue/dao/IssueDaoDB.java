package com.codecool.a38.kanban.issue.dao;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneesIssues;
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
            Assignee assignee = issue.getAssignee();
            if (assignee != null) {
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
    public Map<String, List<Issue>> getIssuesOrderedByStory() {
        Map<String, List<Issue>> issuesOrderedByStory = new HashMap<>();
        issueRepository.findAll().forEach(issue -> {
            Story story = issue.getStory();
            if (story != null) {
                String title = story.getTitle();
                if (!issuesOrderedByStory.containsKey(title)) {
                    issuesOrderedByStory.put(title, new ArrayList<>());
                }
                issuesOrderedByStory.get(title).add(issue);
            }
        });
        return issuesOrderedByStory;
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
