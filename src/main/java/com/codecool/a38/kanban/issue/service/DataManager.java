package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.*;
import com.codecool.a38.kanban.issue.model.graphQLResponse.Milestone;
import com.codecool.a38.kanban.issue.model.graphQLResponse.NodesItem;
import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectsDataResponse;
import com.codecool.a38.kanban.issue.model.transfer.ProjectsData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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

        ProjectsDataResponse projectsDataResponse = gitLabGraphQLCaller.getProjectsDataResponse();

        projectsDataResponse.getData().getProjects().getNodes()
                .forEach((generatedProject) -> {
                    Project thisProject = Project.builder()
                            .projectId(generatedProject.getId())
                            .name(generatedProject.getName())
                            .build();

                    projectsData.getProjects().add(thisProject);

                    generatedProject.getIssues().getNodes()
                            .forEach((generatedIssue) -> {
                                Issue thisIssue = Issue.builder()
                                        .issueId(generatedIssue.getId())
                                        .title(generatedIssue.getTitle())
                                        .description(generatedIssue.getDescription())
                                        .issueUrl(generatedIssue.getWebUrl())
                                        .dueDate(generatedIssue.getDueDate())
                                        .userNotesCount(generatedIssue.getUserNotesCount())
                                        .reference(generatedIssue.getReference())
                                        .project(thisProject)
                                        .mileStone(getMileStone(generatedIssue))
                                        .assignee(getAssignee(generatedIssue))
                                        .build();
                                setStoryPriorityStatus(thisIssue, generatedIssue);

                                projectsData.getMileStones().add()

                            });
                });

        return projectsData;
    }

    private void setStoryPriorityStatus(Issue thisIssue, NodesItem generatedIssue) {
        generatedIssue.getLabels().getNodes().forEach(generatedLabel -> {
            if (generatedLabel.getTitle().startsWith(storyPrefix)) {
                thisIssue.setStory(Story.builder()
                        .labelId(generatedLabel.getId())
                        .title(generatedLabel.getTitle().substring(storyPrefix.length()))
                        .color(generatedLabel.getColor())
                        .build());

            } else if (generatedLabel.getTitle().startsWith(priorityPrefix)) {
                thisIssue.setPriority(Priority.builder()
                        .labelId(generatedLabel.getId())
                        .title(generatedLabel.getTitle().substring(storyPrefix.length()))
                        .color(generatedLabel.getColor())
                        .build());

            } else if (statuses.stream()
                    .anyMatch(existingStatus -> existingStatus.equals(generatedLabel.getTitle()))) {
                thisIssue.setStatus(Status.builder()
                        .labelId(generatedLabel.getId())
                        .title(generatedLabel.getTitle())
                        .color(generatedLabel.getColor())
                        .build());
            }
        });
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

    public ProjectsDataResponse getProjectDataResponse() {
        return gitLabGraphQLCaller.getProjectsDataResponse();
    }

}
