package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.Issue;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.graphQLResponse.*;
import com.codecool.a38.kanban.issue.model.transfer.AssigneesIssues;
import com.codecool.a38.kanban.issue.model.transfer.ProjectsData;
import com.codecool.a38.kanban.issue.model.transfer.StoriesIssues;
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


    public ProjectsData getProjectData() {
        ProjectsData projectsData = new ProjectsData();
        List<Issue> issues = new ArrayList<>();

        gitLabGraphQLCaller.getProjectsDataResponse().getData().getProjects().getNodes()
                .forEach((projectNode) -> {
                    Project thisProject = createProjectFromProjectNode(projectNode);
                    projectsData.addProject(thisProject);

                    projectNode.getIssues().getNodes()
                            .forEach((issueNode) -> {
                                Issue thisIssue = createIssueFromIssueNode(issueNode);
                                thisIssue.setProject(thisProject);
                                thisIssue.setAssignee(getAssignee(issueNode));
                                setStoryPriorityStatus(thisIssue, issueNode);

                                projectsData.addMileStone(thisIssue.getMileStone());
                                projectsData.addStory(thisIssue.getStory());
                                issues.add(thisIssue);
                            });
                });

        removeNullMilestoneStory(projectsData);
        setAssigneesStoriesIssues(projectsData, issues);
        return projectsData;
    }

    private void setAssigneesStoriesIssues(ProjectsData projectsData, List<Issue> issues) {
        Map<Assignee, List<Issue>> issuesOrderedByAssignees = new HashMap<>();
        Map<Label, List<Issue>> issuesOrderedByStory = new HashMap<>();

        issues.forEach(issue -> {
            if (issue.getStatus() != null) {
                Assignee assignee = issue.getAssignee();
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

        projectsData.setAssigneesIssuesList(issuesOrderedByAssignees.entrySet().stream()
                .map(e -> AssigneesIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList()));

        projectsData.setStoriesIssuesList(issuesOrderedByStory.entrySet().stream()
                .map(e -> StoriesIssues.builder()
                        .story(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList()));
    }

    private void removeNullMilestoneStory(ProjectsData projectsData) {
        projectsData.getMileStones().remove(null);
        projectsData.getStories().remove(null);
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

    private Assignee getAssignee(IssueNode issueNode) {
        try {
            return issueNode.getAssignees().getNodes().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public ProjectsDataResponse getProjectDataResponse() {
        return gitLabGraphQLCaller.getProjectsDataResponse();
    }

    public List<ProjectNode> getAllProjects() {
        return gitLabGraphQLCaller.getAllProjects();
    }
}
