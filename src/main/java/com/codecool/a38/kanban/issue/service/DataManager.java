package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.config.service.ConfigDataProvider;
import com.codecool.a38.kanban.issue.model.Issue;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.graphQLResponse.*;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueSetAsignee.IssueSetAssigneesDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.updateIssueData.UpdateIssueDataResponse;
import com.codecool.a38.kanban.issue.model.transfer.AssigneeIssues;
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

    private ConfigDataProvider configDataProvider;

    private DataManagerUtil util;


    public List<String> getStatusTitles() {
        log.info("get status titles");
        return configDataProvider.getStatusTitles();
    }

    public List<AssigneeIssues> getAssigneeIssuesList(String token, Set<String> projectIds,
                                                      Set<String> milestoneTitles, Set<String> storyTitles) {
        if (projectIds == null || projectIds.size() == 0) return null;

        Map<User, List<Issue>> assigneeIssuesMap = new HashMap<>();
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getProjectsIssuesResponse(token, projectIds, milestoneTitles, currentEndCursor)
                    .getData().getProjects();

            currentProjects.getNodes().forEach(projectNode -> {
                Project currentProject = util.createProjectFromProjectNode(projectNode);
                List<IssueNode> issueNodeList = getAllProjectsIssueNodes(token, milestoneTitles, projectNode);
                issueNodeList.forEach(issueNode -> {
                    Issue issue = util.createIssueFromIssueNode(issueNode);
                    issue.setProject(currentProject);

                    if (issue.getStatus() != null) {
                        if (storyTitles != null && storyTitles.size() != 0) {
                            if (issue.getStory() != null && storyTitles.contains(issue.getStory().getTitle())) {
                                addIssueToAssigneeIssuesMap(assigneeIssuesMap, issue);
                            }
                        } else {
                            addIssueToAssigneeIssuesMap(assigneeIssuesMap, issue);
                        }
                    }
                });
            });

            PageInfo projectsPageInfo = currentProjects.getPageInfo();
            currentEndCursor = projectsPageInfo.getEndCursor();
            hasNextPage = projectsPageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get assignee issues list");
        return assigneeIssuesMap.entrySet().stream()
                .map(e -> AssigneeIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private void addIssueToAssigneeIssuesMap(Map<User, List<Issue>> assigneeIssuesMap, Issue issue) {
        User assignee = issue.getAssignee();
        if (!assigneeIssuesMap.containsKey(assignee)) {
            assigneeIssuesMap.put(assignee, new ArrayList<>());
        }
        assigneeIssuesMap.get(assignee).add(issue);
    }

    public List<StoryIssues> getStoryIssuesList(String token, Set<String> projectIds,
                                                Set<String> milestoneTitles, Set<String> storyTitles) {
        if (projectIds == null || projectIds.size() == 0) return null;

        Map<Label, List<Issue>> storyIssuesMap = new HashMap<>();
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getProjectsIssuesResponse(token, projectIds, milestoneTitles, currentEndCursor)
                    .getData().getProjects();

            currentProjects.getNodes().forEach(projectNode -> {
                Project currentProject = util.createProjectFromProjectNode(projectNode);
                List<IssueNode> issueNodeList = getAllProjectsIssueNodes(token, milestoneTitles, projectNode);
                issueNodeList.forEach((issueNode) -> {
                    Issue issue = util.createIssueFromIssueNode(issueNode);
                    issue.setProject(currentProject);

                    if (issue.getStatus() != null) {
                        Label story = issue.getStory();
                        if (storyTitles != null && storyTitles.size() != 0) {
                            if (story != null && storyTitles.contains(story.getTitle())) {
                                addStoryToStoryIssuesMap(storyIssuesMap, issue, story);
                            }
                        } else {
                            addStoryToStoryIssuesMap(storyIssuesMap, issue, story);
                        }
                    }
                });
            });

            PageInfo projectsPageInfo = currentProjects.getPageInfo();
            currentEndCursor = projectsPageInfo.getEndCursor();
            hasNextPage = projectsPageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get story issues list");
        return storyIssuesMap.entrySet().stream()
                .map(e -> StoryIssues.builder()
                        .story(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private void addStoryToStoryIssuesMap(Map<Label, List<Issue>> storyIssuesMap, Issue issue, Label story) {
        if (!storyIssuesMap.containsKey(story)) {
            storyIssuesMap.put(story, new ArrayList<>());
        }
        storyIssuesMap.get(story).add(issue);
    }

    private List<IssueNode> getAllProjectsIssueNodes(String token, Set<String> milestoneTitles, ProjectNode projectNode) {
        Issues issues = projectNode.getIssues();
        List<IssueNode> issueNodeList = issues.getNodes();

        PageInfo issuesPageInfo = issues.getPageInfo();
        if (issuesPageInfo.isHasNextPage()) {
            issueNodeList.addAll(getSingleProjectIssueNodeList(token, projectNode.getFullPath(),
                    milestoneTitles, issuesPageInfo.getEndCursor()));
        }
        return issueNodeList;
    }

    public List<IssueNode> getSingleProjectIssueNodeList(String token, String projectFullPath,
                                                         Set<String> milestoneTitles, String endCursor) {
        List<IssueNode> issueNodeList = new ArrayList<>();
        String currentEndCursor = endCursor;
        boolean hasNextPage;
        do {
            Issues currentIssues = gitLabGraphQLCaller.getSingleProjectIssuesResponse(token, projectFullPath,
                    milestoneTitles, currentEndCursor).getData().getProject().getIssues();

            issueNodeList.addAll(currentIssues.getNodes());

            PageInfo pageInfo = currentIssues.getPageInfo();
            currentEndCursor = pageInfo.getEndCursor();
            hasNextPage = pageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get single project assignee issue node list: " + projectFullPath);
        return issueNodeList;
    }

    public List<Project> getProjects(String token) {
        Set<Project> projects = new HashSet<>();

        String endCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller.getProjectsResponse(token, endCursor)
                    .getData().getProjects();
            currentProjects.getNodes()
                    .forEach(projectNode -> projects.add(util.createProjectFromProjectNode(projectNode)));

            PageInfo pageInfo = currentProjects.getPageInfo();
            endCursor = pageInfo.getEndCursor();
            hasNextPage = pageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get projects");
        return util.getSortedProjects(projects);
    }

    public List<String> getMilestoneTitles(String token, Set<String> projectIds) {
        if (projectIds == null) return null;

        Set<String> milestoneTitles = new HashSet<>();
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getMilestonesResponse(token, projectIds, currentEndCursor).getData().getProjects();

            currentProjects.getNodes().forEach(projectNode -> {
                List<Milestone> milestoneList = new ArrayList<>();
                addProjectMilestones(token, projectNode, milestoneList);

                Group group = projectNode.getGroup();
                if (group != null) {
                    addGroupMilestones(token, milestoneList, group);
                }
                milestoneList.forEach(milestone -> milestoneTitles.add(milestone.getTitle()));
            });

            PageInfo projectsPageInfo = currentProjects.getPageInfo();
            currentEndCursor = projectsPageInfo.getEndCursor();
            hasNextPage = projectsPageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get milestone titles");
        return milestoneTitles.stream().sorted().collect(Collectors.toList());
    }

    private void addProjectMilestones(String token, ProjectNode projectNode, List<Milestone> milestoneList) {
        Milestones projectMilestones = projectNode.getMilestones();
        milestoneList.addAll(projectMilestones.getNodes());

        PageInfo projectMilestonesPageInfo = projectMilestones.getPageInfo();
        if (projectMilestonesPageInfo.isHasNextPage()) {
            milestoneList.addAll(getSingleProjectMilestoneList(token, projectNode.getFullPath(),
                    projectMilestonesPageInfo.getEndCursor()));
        }
    }

    private List<Milestone> getSingleProjectMilestoneList(String token, String projectFullPath, String endCursor) {
        List<Milestone> milestones = new ArrayList<>();
        String currentEndCursor = endCursor;
        boolean hasNextPage;
        do {
            Milestones currentMilestones = gitLabGraphQLCaller
                    .getSingleProjectMilestonesResponse(token, projectFullPath, currentEndCursor)
                    .getData().getProject().getMilestones();

            milestones.addAll(currentMilestones.getNodes());

            PageInfo pageInfo = currentMilestones.getPageInfo();
            hasNextPage = pageInfo.isHasNextPage();
            currentEndCursor = pageInfo.getEndCursor();
        } while (hasNextPage);

        log.info("Add single project milestone list: " + projectFullPath);
        return milestones;
    }

    private void addGroupMilestones(String token, List<Milestone> milestoneList, Group group) {
        Milestones groupMilestones = group.getMilestones();
        milestoneList.addAll(groupMilestones.getNodes());

        PageInfo groupMilestonesPageInfo = groupMilestones.getPageInfo();
        if (groupMilestonesPageInfo.isHasNextPage()) {
            milestoneList.addAll(getSingleGroupMilestoneList(token, group.getFullPath(),
                    groupMilestonesPageInfo.getEndCursor()));
        }
    }

    private List<Milestone> getSingleGroupMilestoneList(String token, String groupFullPath, String endCursor) {
        List<Milestone> milestones = new ArrayList<>();
        String currentEndCursor = endCursor;
        boolean hasNextPage;
        do {
            Milestones currentMilestones = gitLabGraphQLCaller
                    .getSingleGroupMilestonesResponse(token, groupFullPath, currentEndCursor)
                    .getData().getGroup().getMilestones();

            milestones.addAll(currentMilestones.getNodes());

            PageInfo pageInfo = currentMilestones.getPageInfo();
            hasNextPage = pageInfo.isHasNextPage();
            currentEndCursor = pageInfo.getEndCursor();
        } while (hasNextPage);

        log.info("Add single group milestone list: " + groupFullPath);
        return milestones;
    }

    public List<String> getStoryTitles(String token, Set<String> projectIds) {
        if (projectIds == null) return null;

        Set<String> storyTitles = new HashSet<>();
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getProjectsStoryLabelsResponse(token, projectIds, currentEndCursor).getData().getProjects();

            currentProjects.getNodes().forEach(projectNode -> {
                Labels storyLabels = projectNode.getLabels();
                List<Label> storyLabelList = storyLabels.getNodes();

                PageInfo labelsPageInfo = storyLabels.getPageInfo();
                if (labelsPageInfo.isHasNextPage()) {
                    storyLabelList.addAll(getSingleProjectStoryLabelList(token, projectNode.getFullPath(),
                            labelsPageInfo.getEndCursor()));
                }

                storyLabelList.forEach(label -> storyTitles.add(label.getTitle()
                        .substring(configDataProvider.getStoryPrefix().length())));
            });

            PageInfo projectsPageInfo = currentProjects.getPageInfo();
            currentEndCursor = projectsPageInfo.getEndCursor();
            hasNextPage = projectsPageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get story titles");
        return storyTitles.stream().sorted().collect(Collectors.toList());
    }

    private List<Label> getSingleProjectStoryLabelList(String token, String projectFullPath, String endCursor) {
        List<Label> storyLabelList = new ArrayList<>();
        String currentEndCursor = endCursor;
        boolean hasNextPage;
        do {
            Labels currentStoryLabels = gitLabGraphQLCaller
                    .getSingleProjectStoriesResponse(token, projectFullPath, currentEndCursor)
                    .getData().getProject().getLabels();
            storyLabelList.addAll(currentStoryLabels.getNodes());

            PageInfo pageInfo = currentStoryLabels.getPageInfo();
            currentEndCursor = pageInfo.getEndCursor();
            hasNextPage = pageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get single project story label list: " + projectFullPath);
        return storyLabelList;
    }

    public Issue updateStatus(String token, String issueId, String newStatusDisplayTitle) {
        IssueNode issueNode = gitLabGraphQLCaller.getIssueDataResponse(token, issueId).getData().getIssue();
        String issueIid = issueNode.getIid();
        String projectFullPath = issueNode.getDesignCollection().getProject().getFullPath();
        String currentStatusLabelId = util.getStatusLabelId(issueNode);

        String newStatusLabelTitle = configDataProvider.getStatusDisplayTitleMap().get(newStatusDisplayTitle);
        String newStatusLabelId = gitLabGraphQLCaller.
                getProjectLabelDataResponse(token, projectFullPath, newStatusLabelTitle)
                .getData().getProject().getLabel().getId();

        if (!currentStatusLabelId.equals(newStatusLabelId)) {
            String currentStatusLabelIdNum = util.getIdNumValue(currentStatusLabelId);
            String newStatusLabelIdNum = util.getIdNumValue(newStatusLabelId);

            UpdateIssueDataResponse updateIssueDataResponse = gitLabGraphQLCaller.
                    updateStatusLabel(token, projectFullPath, issueIid, currentStatusLabelIdNum, newStatusLabelIdNum);

            log.info("Updated status: " + newStatusDisplayTitle + " of issue: " + issueId);
            return util.createIssueFromIssueNode(updateIssueDataResponse.getData().getUpdateIssue().getIssue());
        }
        log.info("Failed to update status of issue: " + issueId);
        return util.createIssueFromIssueNode(issueNode);
    }

    public Issue updateAssignee(String token, String issueId, String newAssigneeId) {
        IssueNode issueNode = gitLabGraphQLCaller.getIssueDataResponse(token, issueId).getData().getIssue();
        String issueIid = issueNode.getIid();
        String projectFullPath = issueNode.getDesignCollection().getProject().getFullPath();

        String assigneeUsername = "";
        if (!newAssigneeId.equals("unassigned") && !newAssigneeId.equals("")) {
            assigneeUsername = gitLabGraphQLCaller.getUsernameByUserID(token, newAssigneeId)
                    .getData().getUser().getUsername();
        }

        IssueSetAssigneesDataResponse issueSetAssigneesDataResponse = gitLabGraphQLCaller.
                updateAssignee(token, projectFullPath, issueIid, assigneeUsername);
        log.info("Set assignee: " + assigneeUsername + " to issue: " + issueId);
        return util.createIssueFromIssueNode(issueSetAssigneesDataResponse.getData().getIssueSetAssignees().getIssue());
    }

}
