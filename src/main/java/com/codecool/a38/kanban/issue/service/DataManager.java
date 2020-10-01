package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Label;
import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.User;
import com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues.AssigneeIssuesResponse;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.UniversalProjectsData;
import com.codecool.a38.kanban.issue.model.transfer.StoryIssues;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataManager {

    private GitLabGraphQLCaller gitLabGraphQLCaller;

    private static final List<String> statuses = Arrays.asList(
            "Backlog",
            "Todo",
            "Development",
            "Dev review",
            "Final review",
            "Documentation");

    public static List<String> getStatuses() {
        return statuses;
    }

    private static final String priorityPrefix = "Priority: ";

    private static final String storyPrefix = "Story: ";


    public UniversalProjectsData getProjectData() {
        UniversalProjectsData universalProjectsData = new UniversalProjectsData();
        List<Issue> issues = new ArrayList<>();

        gitLabGraphQLCaller.getProjectsDataResponse().getData().getProjects().getNodes()
                .forEach((projectNode) -> {
                    Project thisProject = createProjectFromProjectNode(projectNode);
                    universalProjectsData.addProject(thisProject);

                    projectNode.getIssues().getNodes()
                            .forEach((issueNode) -> {
                                Issue thisIssue = createIssueFromIssueNode(issueNode);
                                thisIssue.setProject(thisProject);
                                thisIssue.setAssignee(getAssignee(issueNode));
                                setStoryPriorityStatus(thisIssue, issueNode);

                                universalProjectsData.addMileStone(thisIssue.getMileStone());
                                universalProjectsData.addStory(thisIssue.getStory());
                                issues.add(thisIssue);
                            });
                });

        removeNullMilestoneStory(universalProjectsData);
        setAssigneesStoriesIssues(universalProjectsData, issues);
        return universalProjectsData;
    }

    private void setAssigneesStoriesIssues(UniversalProjectsData universalProjectsData, List<Issue> issues) {
        Map<User, List<Issue>> issuesOrderedByAssignees = new HashMap<>();
        Map<Label, List<Issue>> issuesOrderedByStory = new HashMap<>();

        issues.forEach(issue -> {
            if (issue.getStatus() != null) {
                User assignee = issue.getAssignee();
                if (!issuesOrderedByAssignees.containsKey(assignee)) {
                    issuesOrderedByAssignees.put(assignee, new ArrayList<>());
                }
                issuesOrderedByAssignees.get(assignee).add(issue);

                Label story = issue.getStory();
                if (story != null) {
                    if (!issuesOrderedByStory.containsKey(story)) {
                        issuesOrderedByStory.put(story, new ArrayList<>());
                    }
                    issuesOrderedByStory.get(story).add(issue);
                }
            }
        });

        universalProjectsData.setAssigneeIssuesList(issuesOrderedByAssignees.entrySet().stream()
                .map(e -> AssigneeIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList()));

        universalProjectsData.setStoryIssuesList(issuesOrderedByStory.entrySet().stream()
                .map(e -> StoryIssues.builder()
                        .story(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList()));
    }

    private void removeNullMilestoneStory(UniversalProjectsData universalProjectsData) {
        universalProjectsData.getMileStones().remove(null);
        universalProjectsData.getStories().remove(null);
    }

    private Project createProjectFromProjectNode(ProjectNode projectNode) {
        return Project.builder()
                .id(projectNode.getId())
                .name(projectNode.getName())
                .build();
    }

    private Issue createIssueFromIssueNode(IssueNode issueNode) {
        return Issue.builder()
                .id(issueNode.getId())
                .title(issueNode.getTitle())
                .description(issueNode.getDescription())
                .webUrl(issueNode.getWebUrl())
                .dueDate(issueNode.getDueDate())
                .userNotesCount(issueNode.getUserNotesCount())
                .reference(issueNode.getReference())
                .mileStone(issueNode.getMilestone())
                .build();
    }

    private void setStoryPriorityStatus(Issue thisIssue, IssueNode issueNode) {
        issueNode.getLabels().getNodes().forEach(label -> {
            if (label.getTitle().startsWith(storyPrefix)) {
                label.setTitle(label.getTitle().substring(storyPrefix.length()));
                thisIssue.setStory(label);
            } else if (label.getTitle().startsWith(priorityPrefix)) {
                label.setTitle(label.getTitle().substring(priorityPrefix.length()));
                thisIssue.setPriority(label);
            } else if (statuses.stream()
                    .anyMatch(existingStatus -> existingStatus.equals(label.getTitle()))) {
                thisIssue.setStatus(label);
            }
        });
    }

    private User getAssignee(IssueNode issueNode) {
        try {
            return issueNode.getAssignees().getNodes().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public AssigneeIssuesResponse getAssigneesIssues() {
        return gitLabGraphQLCaller.getAssigneesIssues();
    }

}
