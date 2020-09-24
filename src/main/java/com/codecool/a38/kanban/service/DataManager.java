package com.codecool.a38.kanban.service;

import com.codecool.a38.kanban.dao.IssueDao;
import com.codecool.a38.kanban.model.Assignee;
import com.codecool.a38.kanban.model.Issue;
import com.codecool.a38.kanban.model.generated.NodesItem;
import com.codecool.a38.kanban.model.generated.ProjectData;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("production")
@AllArgsConstructor
public class DataManager {

    private IssueDao issueDao;

    private GitLabGraphQLCaller gitLabGraphQLCaller;

    public void refreshData() {
        ProjectData projectData = gitLabGraphQLCaller.getProjectData();

        projectData.getData().getProjects().getNodes()
                .forEach((project) -> project.getIssues().getNodes()
                        .forEach((issue) -> {
                            List<String> labels = issue.getLabels().getNodes().stream().
                                    map(NodesItem::getTitle)
                                    .collect(Collectors.toList());

                            issueDao.save(Issue.builder()
                                    .issueId(issue.getId())
                                    .issueTitle(issue.getTitle())
                                    .issueDescription(issue.getDescription())
                                    .projectName(project.getName())
                                    .mileStoneName(issue.getMilestone() != null ?
                                            issue.getMilestone().getTitle() : null)
                                    .issueUrl(issue.getWebUrl())
                                    .assignee(getAssignee(issue))
                                    .labels(labels)
                                    .build());
                        }));
    }

    private Assignee getAssignee(NodesItem issue) {
        try {
            NodesItem assigneeNode = issue.getAssignees().getNodes().get(0);
            return Assignee.builder()
                    .assigneeId(assigneeNode.getId())
                    .name(assigneeNode.getName())
                    .webUrl(assigneeNode.getWebUrl())
                    .avatarUrl(assigneeNode.getAvatarUrl())
                    .build();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @PostConstruct
    public void init() {
        refreshData();
    }

}
