package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.graphQLResponse.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.StoryIssues;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataManager {

    private GitLabGraphQLCaller gitLabGraphQLCaller;

    private static final List<String> STATUS_TITLES = Arrays.asList(
            "Backlog",
            "Todo",
            "Development",
            "Dev review",
            "Final review",
            "Documentation");

    public static List<String> getSTATUS_TITLES() {
        return STATUS_TITLES;
    }

    private static final String priorityPrefix = "Priority: ";

    private static final String storyPrefix = "Story: ";


    public List<AssigneeIssues> getAssigneeIssuesList(List<String> projectIds, List<String> milestoneTitles) {
        Map<User, List<Issue>> assigneeIssuesMap = new HashMap<>();

        gitLabGraphQLCaller.getProjectsIssuesResponse(projectIds, milestoneTitles).getData().getProjects().getNodes()
                .forEach((projectNode) -> {
                    Project thisProject = createProjectFromProjectNode(projectNode);

                    projectNode.getIssues().getNodes()
                            .forEach((issueNode) -> {
                                Issue issue = createIssueFromIssueNode(issueNode);
                                issue.setProject(thisProject);

                                User assignee = issue.getAssignee();
                                if (issue.getStatus() != null) {
                                    if (!assigneeIssuesMap.containsKey(assignee)) {
                                        assigneeIssuesMap.put(assignee, new ArrayList<>());
                                    }
                                    assigneeIssuesMap.get(assignee).add(issue);
                                }
                            });
                });
        return assigneeIssuesMap.entrySet().stream()
                .map(e -> AssigneeIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public List<StoryIssues> getStoryIssuesList(List<String> projectIds, List<String> milestoneTitles) {
        Map<Label, List<Issue>> storyIssuesMap = new HashMap<>();

        gitLabGraphQLCaller.getProjectsIssuesResponse(projectIds, milestoneTitles).getData().getProjects().getNodes()
                .forEach((projectNode) -> {
                    Project thisProject = createProjectFromProjectNode(projectNode);

                    projectNode.getIssues().getNodes()
                            .forEach((issueNode) -> {
                                Issue issue = createIssueFromIssueNode(issueNode);
                                issue.setProject(thisProject);

                                Label story = issue.getStory();
                                if (issue.getStatus() != null && story != null) {
                                    if (!storyIssuesMap.containsKey(story)) {
                                        storyIssuesMap.put(story, new ArrayList<>());
                                    }
                                    storyIssuesMap.get(story).add(issue);
                                }
                            });
                });
        return storyIssuesMap.entrySet().stream()
                .map(e -> StoryIssues.builder()
                        .story(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private Project createProjectFromProjectNode(ProjectNode projectNode) {
        return Project.builder()
                .id(projectNode.getId())
                .name(projectNode.getName())
                .group(projectNode.getGroup())
                .build();
    }

    private Issue createIssueFromIssueNode(IssueNode issueNode) {
        Issue issue = Issue.builder()
                .id(issueNode.getId())
                .title(issueNode.getTitle())
                .description(issueNode.getDescription())
                .webUrl(issueNode.getWebUrl())
                .dueDate(issueNode.getDueDate())
                .userNotesCount(issueNode.getUserNotesCount())
                .reference(issueNode.getReference())
                .mileStone(issueNode.getMilestone())
                .assignee(getAssigneeFromIssueNode(issueNode))
                .build();
        setStoryPriorityStatusOfIssueFromIssueNode(issue, issueNode);
        return issue;
    }

    private void setStoryPriorityStatusOfIssueFromIssueNode(Issue thisIssue, IssueNode issueNode) {
        issueNode.getLabels().getNodes().forEach(label -> {
            if (label.getTitle().startsWith(storyPrefix)) {
                label.setTitle(label.getTitle().substring(storyPrefix.length()));
                thisIssue.setStory(label);
            } else if (label.getTitle().startsWith(priorityPrefix)) {
                label.setTitle(label.getTitle().substring(priorityPrefix.length()));
                thisIssue.setPriority(label);
            } else if (STATUS_TITLES.stream()
                    .anyMatch(existingStatus -> existingStatus.equals(label.getTitle()))) {
                thisIssue.setStatus(label);
            }
        });
    }

    private User getAssigneeFromIssueNode(IssueNode issueNode) {
        try {
            return issueNode.getAssignees().getNodes().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Set<Milestone> getMilestones() {
        Set<Milestone> milestones = new HashSet<>();
        gitLabGraphQLCaller.getMilestonesResponse().getData().getProjects().getNodes()
                .forEach(projectNode -> milestones.addAll(projectNode.getMilestones().getNodes()));
        return milestones;
    }

    public Set<Project> getProjects() {

        return null;
    }
}
