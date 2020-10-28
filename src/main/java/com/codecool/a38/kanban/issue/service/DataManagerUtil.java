package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.config.service.ConfigDataProvider;
import com.codecool.a38.kanban.issue.model.Issue;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Label;
import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataManagerUtil {

    private ConfigDataProvider configDataProvider;

    public Project createProjectFromProjectNode(ProjectNode projectNode) {
        return Project.builder()
                .id(projectNode.getId())
                .fullPath(projectNode.getFullPath())
                .name(projectNode.getName())
                .group(projectNode.getGroup())
                .build();
    }

    public Issue createIssueFromIssueNode(IssueNode issueNode) {
        return Issue.builder()
                .id(issueNode.getId())
                .title(issueNode.getTitle())
                .description(issueNode.getDescription())
                .webUrl(issueNode.getWebUrl())
                .dueDate(issueNode.getDueDate())
                .userNotesCount(issueNode.getUserNotesCount())
                .reference(issueNode.getReference())
                .assignee(getAssigneeFromIssueNode(issueNode))
                .mileStone(issueNode.getMilestone())
                .status(getStatusFromIssueNode(issueNode))
                .story(getStoryFromIssueNode(issueNode))
                .priority(getPriorityFromIssueNode(issueNode))
                .build();
    }

    private Label getStatusFromIssueNode(IssueNode issueNode) {
        for (Label label : issueNode.getLabels().getNodes()) {
            String statusDisplayTitle = configDataProvider.getStatusTitleDisplayMap().get(label.getTitle());
            if (statusDisplayTitle != null) {
                label.setTitle(statusDisplayTitle);
                return label;
            }
        }
        return null;
    }

    private Label getStoryFromIssueNode(IssueNode issueNode) {
        return issueNode.getLabels().getNodes().stream()
                .filter(label -> label.getTitle().startsWith(configDataProvider.getStoryPrefix()))
                .peek(label -> label.setTitle(label.getTitle().substring(configDataProvider.getStoryPrefix().length())))
                .findFirst().orElse(null);
    }

    private Label getPriorityFromIssueNode(IssueNode issueNode) {
        for (Label label : issueNode.getLabels().getNodes()) {
            String priorityDisplayTitle = configDataProvider.getPriorityTitleDisplayMap().get(label.getTitle());
            if (priorityDisplayTitle != null) {
                label.setTitle(priorityDisplayTitle);
                return label;
            }
        }
        return null;
    }

    private User getAssigneeFromIssueNode(IssueNode issueNode) {
        try {
            return issueNode.getAssignees().getNodes().get(0);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    public List<Project> getSortedProjects(Set<Project> projects) {
        return projects.stream()
                .sorted(Comparator.comparing(this::getProjectDisplayName))
                .collect(Collectors.toList());
    }

    private String getProjectDisplayName(Project project) {
        return project.getGroup() != null ?
                project.getGroup().getName() + "/" + project.getName() : project.getName();
    }

    public String getIdNumValue(String currentStatusLabelId) {
        return currentStatusLabelId.substring(currentStatusLabelId.lastIndexOf("/") + 1);
    }

    public String getStatusLabelId(IssueNode issueNode) {
        Label statusLabel = issueNode.getLabels().getNodes().stream()
                .filter(label -> configDataProvider.getStatusTitleDisplayMap().containsKey(label.getTitle()))
                .findFirst().orElse(null);
        return statusLabel != null ? statusLabel.getId() : "";
    }

}
