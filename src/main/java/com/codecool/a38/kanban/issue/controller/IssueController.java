package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.UniversalData;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class IssueController {

    private DataManager dataManager;

    @GetMapping("/universalData")
    public UniversalData getProjectsData() {
        return dataManager.getUniversalData();
    }

    @GetMapping("/issues")
    public AssigneeIssues getIssuesOrderedByAssignee(@RequestParam String userId) {
        return dataManager.getAssigneeIssues(userId);
    }

    @GetMapping("/statuses")
    public List<String> getStatuses() {
        return DataManager.getStatuses();
    }

//    @GetMapping("/issues/orderByAssignee")
//    public AssigneeIssuesResponse getIssuesOrderedByAssignee() {
//        return dataManager.getAssigneesIssues();
//    }

}
