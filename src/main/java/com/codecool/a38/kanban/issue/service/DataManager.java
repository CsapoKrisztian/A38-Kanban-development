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
        log.info("get status titles");
        return statusTitles;
    }

    private static final String priorityPrefix = "Priority: ";

    private static final String storyPrefix = "Story: ";


    public List<AssigneeIssues> getAssigneeIssuesListMax100IssuesPerProject(String token, Filter filter) {
        if (filter.getProjectIds() == null || filter.getMilestoneTitles() == null
                || filter.getStoryTitles() == null) return null;

        Map<User, List<Issue>> assigneeIssuesMap = new HashMap<>();

        gitLabGraphQLCaller.getProjectsIssuesResponse(token, filter.getProjectIds(), filter.getMilestoneTitles())
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

        log.info("Get assignee issues list");
        return assigneeIssuesMap.entrySet().stream()
                .map(e -> AssigneeIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public List<AssigneeIssues> getSingleProjectAssigneeIssuesList(String token, Filter filter) {
        Map<User, List<Issue>> assigneeIssuesMap = new HashMap<>();

        filter.getProjectFullPaths().forEach(projectFullPath -> {
            String endCursor = GitLabGraphQLCaller.getStartPagination();
            boolean hasNextPage;
            do {
                ProjectNode projectNode = gitLabGraphQLCaller.getSingleProjectIssuesResponse(token, projectFullPath,
                        filter.getMilestoneTitles(), endCursor)
                        .getData().getProject();

                Project thisProject = createProjectFromProjectNode(projectNode);

                Issues currentIssues = projectNode.getIssues();
                currentIssues.getNodes()
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

                PageInfo pageInfo = currentIssues.getPageInfo();
                endCursor = pageInfo.getEndCursor();
                hasNextPage = pageInfo.isHasNextPage();
            } while (hasNextPage);
        });

        log.info("Get assignee issues list");
        return assigneeIssuesMap.entrySet().stream()
                .map(e -> AssigneeIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public List<StoryIssues> getStoryIssuesListMax100IssuesPerProject(String token, Filter filter) {
        if (filter.getProjectIds() == null || filter.getMilestoneTitles() == null
                || filter.getStoryTitles() == null) return null;

        Map<Label, List<Issue>> storyIssuesMap = new HashMap<>();
        gitLabGraphQLCaller.getProjectsIssuesResponse(token, filter.getProjectIds(), filter.getMilestoneTitles())
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

        log.info("Get story issues list");
        return storyIssuesMap.entrySet().stream()
                .map(e -> StoryIssues.builder()
                        .story(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public List<StoryIssues> getSingleProjectStoryIssuesList(String token, Filter filter) {
        Map<Label, List<Issue>> storyIssuesMap = new HashMap<>();

        filter.getProjectFullPaths().forEach(projectFullPath -> {
            String endCursor = GitLabGraphQLCaller.getStartPagination();
            boolean hasNextPage;
            do {
                ProjectNode projectNode = gitLabGraphQLCaller.getSingleProjectIssuesResponse(token, projectFullPath,
                        filter.getMilestoneTitles(), endCursor)
                        .getData().getProject();

                Project thisProject = createProjectFromProjectNode(projectNode);
                Issues currentIssues = projectNode.getIssues();

                currentIssues.getNodes()
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

                PageInfo pageInfo = currentIssues.getPageInfo();
                endCursor = pageInfo.getEndCursor();
                hasNextPage = pageInfo.isHasNextPage();
            } while (hasNextPage);
        });

        log.info("Get story issues list");
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
                .fullPath(projectNode.getFullPath())
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

    public Set<Project> getProjects(String token) {
        Set<Project> projects = new HashSet<>();

        String endCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller.getProjectsResponse(token, endCursor).getData().getProjects();
            currentProjects.getNodes()
                    .forEach(projectNode -> projects.add(createProjectFromProjectNode(projectNode)));

            PageInfo pageInfo = currentProjects.getPageInfo();
            endCursor = pageInfo.getEndCursor();
            hasNextPage = pageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get projects");
        return projects;
    }

    public Set<String> getMilestoneTitles(String token, Filter filter) {
        Set<String> milestoneTitles = new HashSet<>();
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getMilestonesResponse(token, filter.getProjectIds(), currentEndCursor).getData().getProjects();
            currentProjects.getNodes()
                    .forEach(projectNode -> {
                        addProjectMilestones(token, milestoneTitles, projectNode);

                        Group group = projectNode.getGroup();
                        if (group != null) {
                            addGroupMileStones(token, milestoneTitles, group);
                        }
                    });

            PageInfo pageInfo = currentProjects.getPageInfo();
            currentEndCursor = pageInfo.getEndCursor();
            hasNextPage = pageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get milestone titles");
        return milestoneTitles;
    }

    private void addProjectMilestones(String token, Set<String> milestoneTitles, ProjectNode projectNode) {
        Milestones projectMilestones = projectNode.getMilestones();
        projectMilestones.getNodes()
                .forEach(milestone -> milestoneTitles.add(milestone.getTitle()));

        PageInfo projectMilestonesPageInfo = projectMilestones.getPageInfo();
        if (projectMilestonesPageInfo.isHasNextPage()) {
            milestoneTitles.addAll(getSingleProjectMilestoneTitles(token, projectNode.getFullPath(),
                    projectMilestonesPageInfo.getEndCursor()));
        }
    }

    private void addGroupMileStones(String token, Set<String> milestoneTitles, Group group) {
        Milestones groupMilestones = group.getMilestones();
        groupMilestones.getNodes()
                .forEach(milestone -> milestoneTitles.add(milestone.getTitle()));

        PageInfo groupMilestonesPageInfo = groupMilestones.getPageInfo();
        if (groupMilestonesPageInfo.isHasNextPage()) {
            milestoneTitles.addAll(getSingleGroupMilestoneTitles(token, group.getFullPath(),
                    groupMilestonesPageInfo.getEndCursor()));
        }
    }

    private Set<String> getSingleProjectMilestoneTitles(String token, String projectFullPath, String endCursor) {
        Set<String> milestoneTitles = new HashSet<>();
        String currentEndCursor = endCursor;
        boolean hasNextPage;
        do {
            Milestones currentMilestones = gitLabGraphQLCaller
                    .getSingleProjectMilestonesResponse(token, projectFullPath, currentEndCursor)
                    .getData().getProject().getMilestones();
            currentMilestones.getNodes().forEach(milestone -> milestoneTitles.add(milestone.getTitle()));

            PageInfo pageInfo = currentMilestones.getPageInfo();
            hasNextPage = pageInfo.isHasNextPage();
            currentEndCursor = pageInfo.getEndCursor();
        } while (hasNextPage);

        log.info("Get single project milestone titles: " + projectFullPath);
        return milestoneTitles;
    }

    private Set<String> getSingleGroupMilestoneTitles(String token, String groupFullPath, String endCursor) {
        Set<String> milestoneTitles = new HashSet<>();
        String currentEndCursor = endCursor;
        boolean hasNextPage;
        do {
            Milestones currentMilestones = gitLabGraphQLCaller
                    .getSingleGroupMilestonesResponse(token, groupFullPath, currentEndCursor)
                    .getData().getGroup().getMilestones();
            currentMilestones.getNodes().forEach(milestone -> milestoneTitles.add(milestone.getTitle()));

            PageInfo pageInfo = currentMilestones.getPageInfo();
            hasNextPage = pageInfo.isHasNextPage();
            currentEndCursor = pageInfo.getEndCursor();
        } while (hasNextPage);

        log.info("Get single group milestone titles: " + groupFullPath);
        return milestoneTitles;
    }

    public Set<String> getStoryTitles(String token, Filter filter) {
        Set<String> storyTitles = new HashSet<>();
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getProjectsStoriesResponse(token, filter.getProjectIds(), currentEndCursor).getData().getProjects();
            currentProjects.getNodes()
                    .forEach(projectNode -> {
                        Labels labels = projectNode.getLabels();
                        labels.getNodes()
                                .forEach(label -> storyTitles.add(label.getTitle().substring(storyPrefix.length())));

                        PageInfo labelsPageInfo = labels.getPageInfo();
                        if (labelsPageInfo.isHasNextPage()) {
                            storyTitles.addAll(getSingleProjectStoryTitles(token, projectNode.getFullPath(),
                                    labelsPageInfo.getEndCursor()));
                        }
                    });

            PageInfo pageInfo = currentProjects.getPageInfo();
            currentEndCursor = pageInfo.getEndCursor();
            hasNextPage = pageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get story titles");
        return storyTitles;
    }

    private Set<String> getSingleProjectStoryTitles(String token, String projectFullPath, String endCursor) {
        Set<String> storyTitles = new HashSet<>();
        String currentEndCursor = endCursor;
        boolean hasNextPage;
        do {
            Labels currentLabels = gitLabGraphQLCaller.getSingleProjectStoriesResponse(token, projectFullPath, currentEndCursor)
                    .getData().getProject().getLabels();
            currentLabels.getNodes().forEach(label -> storyTitles.add(label.getTitle().substring(storyPrefix.length())));

            PageInfo pageInfo = currentLabels.getPageInfo();
            hasNextPage = pageInfo.isHasNextPage();
            currentEndCursor = pageInfo.getEndCursor();
        } while (hasNextPage);

        log.info("Get single project story titles: " + projectFullPath);
        return storyTitles;
    }

}
