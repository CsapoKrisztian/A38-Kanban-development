package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.dao.IssueDao;
import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneesIssues;
import com.codecool.a38.kanban.issue.model.transfer.StoriesIssues;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class IssueController {

    private IssueDao issueDao;

    @GetMapping("/projects")
    public List<Project> getProjects() {
        return issueDao.getProjects();
    }

    @GetMapping("/mileStones")
    public List<MileStone> getMilestones() {
        return issueDao.getMilestones();
    }

    @GetMapping("/stories")
    public List<Story> getStories() {
        return issueDao.getStories();
    }

    @GetMapping("/statuses")
    public List<String> getStatuses() {
        return issueDao.getStatuses();
    }

    @GetMapping("/issues/orderByAssignee")
    public List<AssigneesIssues> getIssuesOrderedByAssignee() {
        return issueDao.getIssuesOrderedByAssignee();
    }

    @GetMapping("/issues/orderByStory")
    public List<StoriesIssues> getIssuesOrderedByStory() {
        return issueDao.getIssuesOrderedByStory();
    }

    @GetMapping("/issues")
    public List<Issue> getIssues() {
        return issueDao.getAll();
    }

}
