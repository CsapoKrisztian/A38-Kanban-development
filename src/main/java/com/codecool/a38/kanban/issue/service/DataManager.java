package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Label;
import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.User;
import com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues.AssigneeIssuesResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues.UserWithMemberships;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.UniversalData;
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


    public UniversalData getUniversalData() {
        UniversalData universalData = new UniversalData();
        List<Issue> issues = new ArrayList<>();

        gitLabGraphQLCaller.getProjectsIssuesResponse().getData().getProjects().getNodes()
                .forEach((projectNode) -> {
                    Project thisProject = createProjectFromProjectNode(projectNode);
                    universalData.addProject(thisProject);

                    projectNode.getIssues().getNodes()
                            .forEach((issueNode) -> {
                                Issue thisIssue = createIssueFromIssueNode(issueNode);
                                thisIssue.setProject(thisProject);
                                thisIssue.setAssignee(getAssigneeFromIssueNode(issueNode));
                                setStoryPriorityStatusOfIssueFromIssueNode(thisIssue, issueNode);

                                universalData.addMileStone(thisIssue.getMileStone());
                                universalData.addStory(thisIssue.getStory());
                                issues.add(thisIssue);
                            });
                });

        removeNullMilestoneStoryOfUniversalData(universalData);
        setAssigneesStoriesIssuesOfUniversalData(universalData, issues);
        return universalData;
    }

    private void setAssigneesStoriesIssuesOfUniversalData(UniversalData universalData, List<Issue> issues) {
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

        universalData.setAssigneeIssuesList(issuesOrderedByAssignees.entrySet().stream()
                .map(e -> AssigneeIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList()));

        universalData.setStoryIssuesList(issuesOrderedByStory.entrySet().stream()
                .map(e -> StoryIssues.builder()
                        .story(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList()));
    }

    private void removeNullMilestoneStoryOfUniversalData(UniversalData universalData) {
        universalData.getMileStones().remove(null);
        universalData.getStories().remove(null);
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

    private void setStoryPriorityStatusOfIssueFromIssueNode(Issue thisIssue, IssueNode issueNode) {
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

    private User getAssigneeFromIssueNode(IssueNode issueNode) {
        try {
            return issueNode.getAssignees().getNodes().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public AssigneeIssues getAssigneeIssues(String userId) {
        AssigneeIssuesResponse assigneeIssuesResponse = gitLabGraphQLCaller.getAssigneeIssuesResponse(userId);
        List<Issue> issues = new ArrayList<>();
        UserWithMemberships userWithMemberships = assigneeIssuesResponse.getData().getUser();

        userWithMemberships.getGroupMemberships().getNodes()
                .forEach(groupMembershipNode -> groupMembershipNode.getGroup().getProjects().getNodes()
                        .forEach(projectNode -> {
                            issues.addAll(generateIssuesFromProjectNode(projectNode));
                        }));
        userWithMemberships.getProjectMemberships().getNodes()
                .forEach(projectMembershipNode -> {
                    issues.addAll(generateIssuesFromProjectNode(projectMembershipNode.getProject()));
                });

        return AssigneeIssues.builder()
                .assignee(User.builder()
                        .id(userWithMemberships.getId())
                        .name(userWithMemberships.getName())
                        .avatarUrl(userWithMemberships.getAvatarUrl())
                        .build())
                .issues(issues)
                .build();
    }

    private List<Issue> generateIssuesFromProjectNode(ProjectNode projectNode) {
        Project thisProject = createProjectFromProjectNode(projectNode);
        List<Issue> issues = new ArrayList<>();

        projectNode.getIssues().getNodes()
                .forEach((issueNode) -> {
                    Issue thisIssue = createIssueFromIssueNode(issueNode);
                    thisIssue.setProject(thisProject);
                    thisIssue.setAssignee(getAssigneeFromIssueNode(issueNode));
                    setStoryPriorityStatusOfIssueFromIssueNode(thisIssue, issueNode);

                    issues.add(thisIssue);
                });
        return issues;
    }

}
