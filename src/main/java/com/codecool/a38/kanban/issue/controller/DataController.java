package com.codecool.a38.kanban.issue.controller;

import com.codecool.a38.kanban.issue.model.generated.ProjectsDataResponse;
import com.codecool.a38.kanban.issue.service.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class DataController {

    private DataManager dataManager;

    @GetMapping("/projectsData")
    public ProjectsDataResponse getProjectsData() {
        return dataManager.getProjectData();
    }

    @PutMapping("/refreshData")
    public void refreshData() {
        dataManager.refreshData();
    }

}
