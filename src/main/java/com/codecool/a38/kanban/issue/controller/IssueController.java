package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.Filter;
import com.codecool.a38.kanban.issue.model.transfer.StoryIssues;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@AllArgsConstructor
public class IssueController {

    private DataManager dataManager;

    @GetMapping("/issues/orderByAssignee")
    public List<AssigneeIssues> getAssigneeIssuesList(@CookieValue String gitlabAccessToken, @RequestBody Filter filter) {
        return dataManager.getAssigneeIssuesList(gitlabAccessToken, filter);
    }

    @PostMapping("/issues/orderByStory")
    public List<StoryIssues> getStoryIssuesList(@CookieValue String gitlabAccessToken, @RequestBody Filter filter) {
        return dataManager.getStoryIssuesList(gitlabAccessToken, filter);
    }

    @GetMapping("/projects")
    public Set<Project> getProjects(@CookieValue String gitlabAccessToken) {
        return dataManager.getProjects(gitlabAccessToken);
    }

    @GetMapping("/milestones")
    public Set<String> getMilestoneTitles(@CookieValue String gitlabAccessToken, @RequestBody Filter filter) {
        return dataManager.getMilestoneTitles(gitlabAccessToken, filter);
    }

    @GetMapping("/stories")
    public Set<String> getStoryTitles(@CookieValue String gitlabAccessToken, @RequestBody Filter filter) {
        return dataManager.getStoryTitles(gitlabAccessToken, filter);
    }

    @GetMapping("/statuses")
    public List<String> getStatusTitles() {
        return DataManager.getStatusTitles();
    }

}
