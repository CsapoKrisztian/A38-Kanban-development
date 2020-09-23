package com.codecool.a38.kanban.controller;

import com.codecool.a38.kanban.model.generated.ProjectData;
import com.codecool.a38.kanban.service.DataManager;
import com.codecool.a38.kanban.service.GitLabGraphQLCaller;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class GitLabController {

    private GitLabGraphQLCaller gitLabGraphQLCaller;

    private DataManager dataManager;

    @GetMapping("/projects")
    public ProjectData getProjectsData() {
        return gitLabGraphQLCaller.getProjectData();
    }

    @PutMapping("/refreshData")
    public void refreshData() {
        dataManager.refreshData();
    }

}
