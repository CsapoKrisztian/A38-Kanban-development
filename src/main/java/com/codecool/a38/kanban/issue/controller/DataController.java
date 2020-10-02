package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectsDataResponse;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class DataController {

    private DataManager dataManager;

    @GetMapping("/projectsDataResponse")
    public ProjectsDataResponse getProjectsDataResponse() {
        return dataManager.getProjectDataResponse();
    }

    @GetMapping("/projects")
    public List<ProjectNode> getAllProjects() {
        return dataManager.getAllProjects();
    }
}
