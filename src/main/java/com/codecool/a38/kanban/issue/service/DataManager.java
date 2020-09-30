package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.*;
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
                    Project thisProject = createRealProject(projectNode);
                    projectsData.addProject(thisProject);

                    projectNode.getIssues().getNodes()
                            .forEach((issueNode) -> {
                                Issue thisIssue = createRealIssue(issueNode);

                                thisIssue.setProject(thisProject);
                                thisIssue.setAssignee(getAssignee(issueNode));

                                Milestone mileStone = getMileStone(issueNode);
                                projectsData.addMileStone(mileStone);
                                thisIssue.setMileStone(mileStone);

                                setStoryPriorityStatus(thisIssue, issueNode);
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
        Map<Story, List<Issue>> issuesOrderedByStory = new HashMap<>();

        issues.forEach(issue -> {
            if (issue.getStatus() != null) {
                Assignee assignee = issue.getAssignee();
                if (!issuesOrderedByAssignees.containsKey(assignee)) {
                    issuesOrderedByAssignees.put(assignee, new ArrayList<>());
                }
                issuesOrderedByAssignees.get(assignee).add(issue);

                Story story = issue.getStory();
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

    private Project createRealProject(ProjectNode projectNode) {
        return Project.builder()
                .id(projectNode.getId())
                .name(projectNode.getName())
                .build();
    }

    private Issue createRealIssue(IssueNode issueNode) {
        return Issue.builder()
                .id(issueNode.getId())
                .title(issueNode.getTitle())
                .description(issueNode.getDescription())
                .webUrl(issueNode.getWebUrl())
                .dueDate(issueNode.getDueDate())
                .userNotesCount(issueNode.getUserNotesCount())
                .reference(issueNode.getReference())
                .build();
    }

    private void setStoryPriorityStatus(Issue thisIssue, IssueNode issueNode) {
        issueNode.getLabels().getNodes().forEach(generatedLabel -> {
            if (generatedLabel.getTitle().startsWith(storyPrefix)) {
                thisIssue.setStory(Story.builder()
                        .id(generatedLabel.getId())
                        .title(generatedLabel.getTitle().substring(storyPrefix.length()))
                        .color(generatedLabel.getColor())
                        .build());

            } else if (generatedLabel.getTitle().startsWith(priorityPrefix)) {
                thisIssue.setPriority(Priority.builder()
                        .id(generatedLabel.getId())
                        .title(generatedLabel.getTitle().substring(storyPrefix.length()))
                        .color(generatedLabel.getColor())
                        .build());

            } else if (statuses.stream()
                    .anyMatch(existingStatus -> existingStatus.equals(generatedLabel.getTitle()))) {
                thisIssue.setStatus(Status.builder()
                        .id(generatedLabel.getId())
                        .title(generatedLabel.getTitle())
                        .color(generatedLabel.getColor())
                        .build());
            }
        });
    }

    private Milestone getMileStone(IssueNode issueNode) {
        try {
            Milestone milestoneNode = issueNode.getMilestone();
            return Milestone.builder()
                    .id(milestoneNode.getId())
                    .title(milestoneNode.getTitle())
                    .build();
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Assignee getAssignee(IssueNode issueNode) {
        try {
            Assignee assigneeNode = issueNode.getAssignees().getNodes().get(0);
            return Assignee.builder()
                    .id(assigneeNode.getId())
                    .name(assigneeNode.getName())
                    .avatarUrl(assigneeNode.getAvatarUrl())
                    .build();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public ProjectsDataResponse getProjectDataResponse() {
        return gitLabGraphQLCaller.getProjectsDataResponse();
    }

}
