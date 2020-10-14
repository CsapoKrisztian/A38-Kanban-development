package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.Issue;
import com.codecool.a38.kanban.issue.model.transfer.UpdateAssigneeRequestBody;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.transfer.UpdateStatusRequestBody;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.FilterRequestBody;
import com.codecool.a38.kanban.issue.model.transfer.StoryIssues;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class IssueController {

    private DataManager dataManager;

    @PostMapping("/issues/orderByAssignee")
    public List<AssigneeIssues> getAssigneeIssuesList(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                                      @RequestBody FilterRequestBody filter) {
        return dataManager.getAssigneeIssuesList(gitlabAccessToken, filter.getProjectIds(),
                filter.getMilestoneTitles(), filter.getStoryTitles());
    }

    @PostMapping("/issues/orderByStory")
    public List<StoryIssues> getStoryIssuesList(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                                @RequestBody FilterRequestBody filter) {
        return dataManager.getStoryIssuesList(gitlabAccessToken, filter.getProjectIds(),
                filter.getMilestoneTitles(), filter.getStoryTitles());
    }

    @GetMapping("/projects")
    public Set<Project> getProjects(@CookieValue(defaultValue = "default") String gitlabAccessToken) {
        return dataManager.getProjects(gitlabAccessToken);
    }

    @PostMapping("/milestones")
    public Set<String> getMilestoneTitles(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                          @RequestBody FilterRequestBody filter) {
        return dataManager.getMilestoneTitles(gitlabAccessToken, filter.getProjectIds());
    }

    @PostMapping("/stories")
    public Set<String> getStoryTitles(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                                      @RequestBody FilterRequestBody filter) {
        return dataManager.getStoryTitles(gitlabAccessToken, filter.getProjectIds());
    }

    @GetMapping("/statuses")
    public List<String> getStatusTitles() {
        return dataManager.getStatusTitles();
    }

    @PostMapping("/updateStatus")
    public Issue updateStatus(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                              @RequestBody UpdateStatusRequestBody updateStatusRequestBody) {
        return dataManager.updateStatus(gitlabAccessToken, updateStatusRequestBody.getIssueId(),
                updateStatusRequestBody.getNewStatusTitle());
    }

    @PostMapping("/updateAssignee")
    public void updateAssignee(@CookieValue(defaultValue = "default") String gitlabAccessToken,
                               @RequestBody UpdateAssigneeRequestBody updateAssigneeRequestBody) {
        dataManager.updateAssignee(gitlabAccessToken, updateAssigneeRequestBody.getNewAssigneeId(),
                updateAssigneeRequestBody.getIssueId());
    }
}
