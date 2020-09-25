package com.codecool.a38.kanban.issue.dao;

import com.codecool.a38.kanban.issue.model.Assignee;
import com.codecool.a38.kanban.issue.model.Issue;
import com.codecool.a38.kanban.issue.model.MileStone;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.repository.IssueRepository;
import com.codecool.a38.kanban.issue.repository.MileStoneRepository;
import com.codecool.a38.kanban.issue.repository.ProjectRepository;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class IssueDaoDB implements IssueDao {

    private IssueRepository issueRepository;

    private ProjectRepository projectRepository;

    private MileStoneRepository mileStoneRepository;

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
    public Map<Assignee, List<Issue>> getIssuesOrderedByAssignee() {
        Map<Assignee, List<Issue>> issuesOrderedByAssignees = new HashMap<>();
        issueRepository.findAll().forEach(issue -> {
            if (issue.getAssignee() != null) {
                issuesOrderedByAssignees.getOrDefault(issue.getAssignee(), new ArrayList<>()).add(issue);
            }
        });
        return issuesOrderedByAssignees;
    }

    @Override
    public Map<String, List<Issue>> getIssuesOrderedByStory() {
        Map<String, List<Issue>> issuesOrderedByStory = new HashMap<>();
        issueRepository.findAll().forEach(issue -> {
            if (issue.getAssignee() != null) {
                issuesOrderedByStory.getOrDefault(issue.getStory(), new ArrayList<>()).add(issue);
            }
        });
        return issuesOrderedByStory;
    }

    @Override
    public void save(Issue issue) {
        log.info("Save issue: " + issue);
        issueRepository.save(issue);
    }

}
