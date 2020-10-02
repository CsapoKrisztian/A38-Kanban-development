package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.graphQLResponse.Milestone;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.FilterProjectsMilestones;
import com.codecool.a38.kanban.issue.model.transfer.StoryIssues;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class IssueController {

    private DataManager dataManager;

    @GetMapping("/issues/orderByAssignee")
    public List<AssigneeIssues> getAssigneeIssuesList(@RequestBody FilterProjectsMilestones filter) {
        return dataManager.getAssigneeIssuesList(filter.getProjectIds(), filter.getMilestoneTitles());
    }

    @GetMapping("/issues/orderByStory")
    public List<StoryIssues> getStoryIssuesList(@RequestBody FilterProjectsMilestones filter) {
        return dataManager.getStoryIssuesList(filter.getProjectIds(), filter.getMilestoneTitles());
    }

    @GetMapping("/statuses")
    public List<String> getStatuses() {
        return DataManager.getSTATUS_TITLES();
    }

    @GetMapping("/milestones")
    public List<Milestone> getMilestones() {
        return dataManager.getMilestones();
    }

}
