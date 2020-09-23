package com.codecool.a38.kanban.service;

import com.codecool.a38.kanban.dao.IssueDao;
import com.codecool.a38.kanban.model.Issue;
import com.codecool.a38.kanban.model.generated.ProjectData;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("production")
@AllArgsConstructor
public class DataManager {

    private IssueDao issueDao;

    private GitLabGraphQLCaller gitLabGraphQLCaller;

    public void refreshData() {
        ProjectData projectData = gitLabGraphQLCaller.getProjectData();

        projectData.getData().getProjects().getNodes().forEach((project) -> {
                    project.getIssues().getNodes().forEach((issue) -> {
                        issueDao.save(Issue.builder()
                                .issueId(issue.getId())
                                .issueTitle(issue.getTitle())
                                .issueDescription(issue.getDescription())
                                .issueUrl(issue.getWebUrl())
                        .build());
                    });
                });
    }

    @PostConstruct
    public void init() {
        refreshData();
    }

}
