package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.dao.IssueDao;
import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.generated.Milestone;
import com.codecool.a38.kanban.issue.model.generated.NodesItem;
import com.codecool.a38.kanban.issue.model.generated.ProjectsDataResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataManager {

    private IssueDao issueDao;

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

    public void refreshData() {
        ProjectsDataResponse projectsDataResponse = gitLabGraphQLCaller.getProjectData();

        projectsDataResponse.getData().getProjects().getNodes()
                .forEach((generatedProject) -> {
                    Project thisProject = Project.builder()
                            .projectId(generatedProject.getId())
                            .name(generatedProject.getName())
                            .build();

                    generatedProject.getIssues().getNodes()
                            .forEach((generatedIssue) -> {

                                String story = null, priority = null, status = null;
                                List<Label> labels = getLabels(generatedIssue);
                                ListIterator<Label> iterator = labels.listIterator();
                                while (iterator.hasNext()) {
                                    Label label = iterator.next();
                                    if (label.getTitle().startsWith(storyPrefix)) {
                                        story = label.getTitle().substring(storyPrefix.length());
                                        iterator.remove();
                                    } else if (label.getTitle().startsWith(priorityPrefix)) {
                                        priority = label.getTitle().substring(priorityPrefix.length());
                                        iterator.remove();
                                    } else if (statuses.stream()
                                            .anyMatch(existingStatus -> existingStatus.equals(label.getTitle()))) {
                                        status = label.getTitle();
                                        iterator.remove();
                                    }
                                }

                                issueDao.save(Issue.builder()
                                        .issueId(generatedIssue.getId())
                                        .title(generatedIssue.getTitle())
                                        .description(generatedIssue.getDescription())
                                        .story(story)
                                        .status(status)
                                        .priority(priority)
                                        .issueUrl(generatedIssue.getWebUrl())
                                        .dueDate(generatedIssue.getDueDate())
                                        .userNotesCount(generatedIssue.getUserNotesCount())
                                        .project(thisProject)
                                        .mileStone(getMileStone(generatedIssue))
                                        .assignee(getAssignee(generatedIssue))
                                        .labels(labels)
                                        .build());
                            });
                });
    }

    private List<Label> getLabels(NodesItem generatedIssue) {
        return generatedIssue.getLabels().getNodes().stream()
                .map(generatedLabel -> Label.builder()
                        .labelId(generatedLabel.getId())
                        .title(generatedLabel.getTitle())
                        .color(generatedIssue.getColor())
                        .build())
                .collect(Collectors.toList());
    }

    private MileStone getMileStone(NodesItem generatedIssue) {
        try {
            Milestone milestoneNode = generatedIssue.getMilestone();
            return MileStone.builder()
                    .mileStoneId(milestoneNode.getId())
                    .title(milestoneNode.getTitle())
                    .build();
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Assignee getAssignee(NodesItem generatedIssue) {
        try {
            NodesItem assigneeNode = generatedIssue.getAssignees().getNodes().get(0);
            return Assignee.builder()
                    .assigneeId(assigneeNode.getId())
                    .name(assigneeNode.getName())
                    .avatarUrl(assigneeNode.getAvatarUrl())
                    .build();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public ProjectsDataResponse getProjectData() {
        return gitLabGraphQLCaller.getProjectData();
    }

}
