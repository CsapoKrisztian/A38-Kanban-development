package com.codecool.a38.kanban.controller;

import com.codecool.a38.kanban.model.generated.ProjectData;
import com.codecool.a38.kanban.service.GitLabGraphQLCaller;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class GitLabController {

    private GitLabGraphQLCaller gitLabGraphQLCaller;

    @GetMapping("/projects")
    public ProjectData getProjectsData() {
        return gitLabGraphQLCaller.getProjectData();
    }

}
