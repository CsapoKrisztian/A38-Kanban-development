package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.Filter;
import com.codecool.a38.kanban.issue.model.transfer.StoryIssues;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class IssueController {

    private DataManager dataManager;

    @GetMapping("/issues/orderByAssignee")
    public List<AssigneeIssues> getAssigneeIssuesList(@RequestBody Filter filter) {
        return dataManager.getAssigneeIssuesList(filter);
    }

    @PostMapping("/issues/orderByStory")
    public List<StoryIssues> getStoryIssuesList(@RequestBody Filter filter,
                                                @CookieValue(value = "gitlabAccessToken", defaultValue = "defaultValue")
                                                        String gitlabAccessToken,
                                                HttpServletRequest request) {
        System.out.println("cookie gitlabAccessToken: " + gitlabAccessToken);

        return dataManager.getStoryIssuesList(filter);
    }

    @GetMapping("/projects")
    public Set<Project> getProjects() {
        return dataManager.getProjects();
    }

    @GetMapping("/milestones")
    public Set<String> getMilestoneTitles(@RequestBody Filter filter) {
        return dataManager.getMilestoneTitles(filter);
    }

    @GetMapping("/stories")
    public Set<String> getStoryTitles(@RequestBody Filter filter) {
        return dataManager.getStoryTitles(filter);
    }

    @GetMapping("/statuses")
    public List<String> getStatusTitles() {
        return DataManager.getStatusTitles();
    }

}
