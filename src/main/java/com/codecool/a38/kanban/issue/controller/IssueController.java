package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.ChangeAssigneeRequest;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.UpdateIssueRequestBody;
import com.codecool.a38.kanban.issue.model.requestBodies.GetAllIssuesRequestBody;
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

    @PostMapping("/issues/orderByAssignee")
    public List<AssigneeIssues> getAssigneeIssuesList(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                                      @RequestBody Filter filter) {
        return dataManager.getAssigneeIssuesList(gitlabAccessToken, filter.getProjectIds(),
                filter.getMilestoneTitles(), filter.getStoryTitles());
    }

    @PostMapping("/issues/orderByStory")
    public List<StoryIssues> getStoryIssuesList(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                                @RequestBody Filter filter) {
        return dataManager.getStoryIssuesList(gitlabAccessToken, filter.getProjectIds(),
                filter.getMilestoneTitles(), filter.getStoryTitles());
    }

    @GetMapping("/projects")
    public Set<Project> getProjects(@CookieValue(defaultValue = "default") String gitlabAccessToken) {
        return dataManager.getProjects(gitlabAccessToken);
    }

    @PostMapping("/milestones")
    public Set<String> getMilestoneTitles(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                          @RequestBody Filter filter) {
        return dataManager.getMilestoneTitles(gitlabAccessToken, filter.getProjectIds());
    }

    @PostMapping("/stories")
    public Set<String> getStoryTitles(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                      @RequestBody Filter filter) {
        return dataManager.getStoryTitles(gitlabAccessToken, filter.getProjectIds());
    }

    @GetMapping("/statuses")
    public List<String> getStatusTitles() {
        return DataManager.getStatusTitles();
    }

    @PostMapping("/update")
    public void updateIssue(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                            @RequestBody UpdateIssueRequestBody data) {
        dataManager.updateIssue(gitlabAccessToken, data);
    }

    @PostMapping("/newAssignee")
    public void changeAssignee(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                               @RequestBody ChangeAssigneeRequest data) {
        dataManager.changeAssignee(gitlabAccessToken, data.getAssignee(), data.getIssueID());
    }

    @PostMapping("/issuesByProject")
    public List<com.codecool.a38.kanban.issue.model.graphQLResponse.projects.projectAllIssues.NodesItem> getAllIssuesByProject(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                                                                                                               @RequestBody GetAllIssuesRequestBody data) {
        //if (data.getProjectIDs().size() != 0){
        return dataManager.getAllIssuesFromProject(gitlabAccessToken, data.getProjectIDs());

    }

}
