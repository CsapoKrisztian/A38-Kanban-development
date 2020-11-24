package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.config.model.PriorityDisplayNum;
import com.codecool.a38.kanban.config.service.ConfigDataProvider;
import com.codecool.a38.kanban.issue.model.Issue;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Label;
import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.User;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.StoryIssues;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataManagerUtil {

    private ConfigDataProvider configDataProvider;

    public Project makeProjectFromProjectNode(ProjectNode projectNode) {
        return Project.builder()
                .id(projectNode.getId())
                .fullPath(projectNode.getFullPath())
                .name(projectNode.getName())
                .group(projectNode.getGroup())
                .build();
    }

    public Issue makeIssueFromIssueNode(IssueNode issueNode) {
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
        return issueNode.getLabels().getNodes().stream()
                .filter(label -> configDataProvider.getStatusTitleDisplayMap().containsKey(label.getTitle()))
                .findFirst().orElse(null);
    }

    private Label getStoryFromIssueNode(IssueNode issueNode) {
        return issueNode.getLabels().getNodes().stream()
                .filter(label -> label.getTitle().startsWith(configDataProvider.getStoryPrefix()))
                .peek(label -> label.setTitle(label.getTitle().substring(configDataProvider.getStoryPrefix().length())))
                .findFirst().orElse(null);
    }

    private Label getPriorityFromIssueNode(IssueNode issueNode) {
        for (Label label : issueNode.getLabels().getNodes()) {
            PriorityDisplayNum priorityDisplayNum = configDataProvider.getPriorityTitleDisplayNumMap()
                    .get(label.getTitle());
            if (priorityDisplayNum != null) {
                label.setTitle(priorityDisplayNum.getDisplay());
                label.setPriorityNum(priorityDisplayNum.getPriorityNum());
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

    public void putIssueToAssigneeIssueMap(Set<String> storyTitles, Map<User, List<Issue>> assigneeIssuesMap, Issue issue) {
        if (storyTitles != null && storyTitles.size() != 0) {
            if (issue.getStory() != null && storyTitles.contains(issue.getStory().getTitle())) {
                addIssueToAssigneeIssuesMap(assigneeIssuesMap, issue);
            }
        } else {
            addIssueToAssigneeIssuesMap(assigneeIssuesMap, issue);
        }
    }

    private void addIssueToAssigneeIssuesMap(Map<User, List<Issue>> assigneeIssuesMap, Issue issue) {
        User assignee = issue.getAssignee();
        if (!assigneeIssuesMap.containsKey(assignee)) {
            assigneeIssuesMap.put(assignee, new ArrayList<>());
        }
        assigneeIssuesMap.get(assignee).add(issue);
    }

    public void putIssueToStoryIssueMap(Set<String> storyTitles, Map<Label, List<Issue>> storyIssuesMap, Issue issue) {
        Label story = issue.getStory();
        if (storyTitles != null && storyTitles.size() != 0) {
            if (story != null && storyTitles.contains(story.getTitle())) {
                addStoryToStoryIssuesMap(storyIssuesMap, issue, story);
            }
        } else {
            addStoryToStoryIssuesMap(storyIssuesMap, issue, story);
        }
    }

    private void addStoryToStoryIssuesMap(Map<Label, List<Issue>> storyIssuesMap, Issue issue, Label story) {
        if (!storyIssuesMap.containsKey(story)) {
            storyIssuesMap.put(story, new ArrayList<>());
        }
        storyIssuesMap.get(story).add(issue);
    }

    public Map<String, AssigneeIssues> makeAssigneeIdIssuesMap(Map<User, List<Issue>> assigneeIssuesMap) {
        return assigneeIssuesMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey() != null ? e.getKey().getId() : "",
                        e -> AssigneeIssues.builder()
                                .assignee(e.getKey())
                                .statusIssuesMap(makeStatusIssuesMap(e.getValue()))
                                .build()));
    }

    public Map<String, StoryIssues> makeStoryIdIssuesMap(Map<Label, List<Issue>> storyIssuesMap) {
        return storyIssuesMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey() != null ? e.getKey().getId() : "",
                        e -> StoryIssues.builder()
                                .story(e.getKey())
                                .statusIssuesMap(makeStatusIssuesMap(e.getValue()))
                                .build()));
    }

    private LinkedHashMap<String, List<Issue>> makeStatusIssuesMap(List<Issue> issues) {
        LinkedHashMap<String, List<Issue>> statusIssuesMap = new LinkedHashMap<>();

        statusIssuesMap.put("Backlog", issues.stream()
                .filter(issue -> issue.getStatus() == null)
                .sorted()
                .collect(Collectors.toList()));

        configDataProvider.getStatusTitleDisplayMap().keySet().forEach((statusTitle) -> statusIssuesMap.put(
                statusTitle, issues.stream()
                        .filter(issue -> issue.getStatus() != null && issue.getStatus().getTitle().equals(statusTitle))
                        .sorted()
                        .collect(Collectors.toList())
        ));
        return statusIssuesMap;
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
