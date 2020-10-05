package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.graphQLResponse.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
import com.codecool.a38.kanban.issue.model.transfer.Filter;
import com.codecool.a38.kanban.issue.model.transfer.StoryIssues;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class DataManager {

    private GitLabGraphQLCaller gitLabGraphQLCaller;

    private static final List<String> statusTitles = Arrays.asList(
            "Backlog",
            "Todo",
            "Development",
            "Dev review",
            "Final review",
            "Documentation");

    public static List<String> getStatusTitles() {
        return statusTitles;
    }

    private static final String priorityPrefix = "Priority: ";

    private static final String storyPrefix = "Story: ";


    public List<AssigneeIssues> getAssigneeIssuesList(Filter filter) {
        if (filter.getProjectIds() == null || filter.getMilestoneTitles() == null
                || filter.getStoryTitles() == null) return null;

        Map<User, List<Issue>> assigneeIssuesMap = new HashMap<>();

        gitLabGraphQLCaller.getProjectsIssuesResponse(filter.getProjectIds(), filter.getMilestoneTitles())
                .getData().getProjects().getNodes()
                .forEach((projectNode) -> {
                    Project thisProject = createProjectFromProjectNode(projectNode);

                    projectNode.getIssues().getNodes()
                            .forEach((issueNode) -> {
                                Issue issue = createIssueFromIssueNode(issueNode);
                                issue.setProject(thisProject);

                                if (issue.getStatus() != null && issue.getStory() != null
                                        && filter.getStoryTitles().contains(issue.getStory().getTitle())) {
                                    User assignee = issue.getAssignee();
                                    if (!assigneeIssuesMap.containsKey(assignee)) {
                                        assigneeIssuesMap.put(assignee, new ArrayList<>());
                                    }
                                    assigneeIssuesMap.get(assignee).add(issue);
                                }
                            });
                });
        log.info("Get assigneeIssuesList");
        return assigneeIssuesMap.entrySet().stream()
                .map(e -> AssigneeIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public List<StoryIssues> getStoryIssuesList(Filter filter) {
        if (filter.getProjectIds() == null || filter.getMilestoneTitles() == null
                || filter.getStoryTitles() == null) return null;

        Map<Label, List<Issue>> storyIssuesMap = new HashMap<>();
        gitLabGraphQLCaller.getProjectsIssuesResponse(filter.getProjectIds(), filter.getMilestoneTitles())
                .getData().getProjects().getNodes()
                .forEach((projectNode) -> {
                    Project thisProject = createProjectFromProjectNode(projectNode);

                    projectNode.getIssues().getNodes()
                            .forEach((issueNode) -> {
                                Issue issue = createIssueFromIssueNode(issueNode);
                                issue.setProject(thisProject);

                                Label story = issue.getStory();
                                if (issue.getStatus() != null && story != null
                                        && filter.getStoryTitles().contains(story.getTitle())) {
                                    if (!storyIssuesMap.containsKey(story)) {
                                        storyIssuesMap.put(story, new ArrayList<>());
                                    }
                                    storyIssuesMap.get(story).add(issue);
                                }
                            });
                });
        log.info("Get storyIssuesList");
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
            } else if (statusTitles.stream()
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

    public Set<Project> getProjects() {
        Set<Project> projects = new HashSet<>();
        gitLabGraphQLCaller.getProjectsResponse().getData().getProjects().getNodes()
                .forEach(projectNode -> projects.add(
                        Project.builder()
                                .id(projectNode.getId())
                                .name(projectNode.getName())
                                .group(projectNode.getGroup())
                                .build()));
        return projects;
    }

    public Set<String> getMilestoneTitles() {
        Set<String> milestones = new HashSet<>();
        gitLabGraphQLCaller.getMilestonesResponse().getData().getProjects().getNodes()
                .forEach(projectNode -> projectNode.getMilestones().getNodes()
                        .forEach(milestone -> milestones.add(milestone.getTitle())));
        return milestones;
    }

    public Set<String> getStoryTitles() {
        Set<String> stories = new HashSet<>();
        gitLabGraphQLCaller.getStoriesResponse().getData().getProjects().getNodes()
                .forEach(projectNode -> projectNode.getLabels().getNodes()
                        .forEach(label -> stories.add(label.getTitle().substring(storyPrefix.length()))));
        return stories;
    }

}
