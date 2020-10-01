package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneesIssues;
import com.codecool.a38.kanban.issue.model.transfer.ProjectsData;
import com.codecool.a38.kanban.issue.model.transfer.StoriesIssues;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class IssueController {

    private DataManager dataManager;

    @GetMapping("/statuses")
    public List<String> getStatuses() {
        return DataManager.getStatuses();
    }

    @GetMapping("/projectsData")
    public ProjectsData getProjectsData() {
        return dataManager.getProjectData();
    }

    @GetMapping("/issues/orderByAssignee")
    public String getIssuesOrderedByAssignee() {
        return dataManager.getAssigneesIssues();
    }

}
