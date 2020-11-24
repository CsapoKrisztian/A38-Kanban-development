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


    public List<String> getStatusDisplayTitles() {
        log.info("get status titles");
        return configDataProvider.getStatusDisplayTitles();
    }

    /**
     * Returns the issues in a nested map, in which the issues are grouped together first by their assignee and then by their status.
     * The keys of the outer map are the ids of assignees, the keys of the inner map are the statuses.
     * This arrangement helps the frontend to display the issues on a board,
     * and to easily find and manipulate particular issues when the drag and drop function is used.
     * The issues are filtered by project ids, milestone titles and story titles.
     * Since the API restricts the number of nodes that can be received from it, a nested pagination is used.
     * The outer pagination paginates over the projects,
     * and if it is necessary for a particular project, then inner pagination paginates over the issues of that project.
     *
     * @param token           the token that is used in request to gitlab API for the authentication
     * @param projectIds      the project ids by which the issues are filtered.
     * @param milestoneTitles the milestone titles by which the issues are filtered.
     *                        If no milestone titles are given, then this filter is ignored.
     * @param storyTitles     the story titles by which the issues are filtered.
     *                        If no story titles titles are given, then this filter is ignored.
     * @return the map containing the issues in an ordered way
     */
    public Map<String, AssigneeIssues> getAssigneeIdIssuesMap(String token, Set<String> projectIds,
                                                              Set<String> milestoneTitles, Set<String> storyTitles) {
        if (projectIds == null || projectIds.size() == 0) return null;

        Map<User, List<Issue>> assigneeIssuesMap = new HashMap<>();
        String currentEndCursor = gitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getProjectsIssuesResponse(token, projectIds, milestoneTitles, currentEndCursor)
                    .getData().getProjects();

            currentProjects.getNodes().forEach(projectNode -> {
                Project currentProject = util.makeProjectFromProjectNode(projectNode);
                List<IssueNode> issueNodeList = getAllProjectsIssueNodes(token, milestoneTitles, projectNode);
                issueNodeList.forEach(issueNode -> {
                    Issue issue = util.makeIssueFromIssueNode(issueNode);
                    issue.setProject(currentProject);
                    util.putIssueToAssigneeIssueMap(storyTitles, assigneeIssuesMap, issue);
                });
            });

            PageInfo projectsPageInfo = currentProjects.getPageInfo();
            currentEndCursor = projectsPageInfo.getEndCursor();
            hasNextPage = projectsPageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get assignee issues list");
        return util.makeAssigneeIdIssuesMap(assigneeIssuesMap);
    }

    /**
     * Returns the issues in a nested map, in which the issues are grouped together first by their story and then by their status.
     * The keys of the outer map are the ids of stories, the keys of the inner map are the statuses.
     * This arrangement helps the frontend to display the issues on a board,
     * and to easily find and manipulate particular issues when the drag and drop function is used.
     * The issues are filtered by project ids, milestone titles and story titles.
     * Since the API restricts the number of nodes that can be received from it, a nested pagination is used.
     * The outer pagination paginates over the projects,
     * and if it is necessary for a particular project, then inner pagination paginates over the issues of that project.
     *
     * @param token           the token that is used in request to gitlab API for the authentication
     * @param projectIds      the project ids by which the issues are filtered.
     * @param milestoneTitles the milestone titles by which the issues are filtered.
     *                        If no milestone titles are given, then this filter is ignored.
     * @param storyTitles     the story titles by which the issues are filtered.
     *                        If no story titles titles are given, then this filter is ignored.
     * @return the map containing the issues in an ordered way
     */
    public Map<String, StoryIssues> getStoryIdIssuesMap(String token, Set<String> projectIds,
                                                        Set<String> milestoneTitles, Set<String> storyTitles) {
        if (projectIds == null || projectIds.size() == 0) return null;

        Map<Label, List<Issue>> storyIssuesMap = new HashMap<>();
        String currentEndCursor = gitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getProjectsIssuesResponse(token, projectIds, milestoneTitles, currentEndCursor)
                    .getData().getProjects();

            currentProjects.getNodes().forEach(projectNode -> {
                Project currentProject = util.makeProjectFromProjectNode(projectNode);
                List<IssueNode> issueNodeList = getAllProjectsIssueNodes(token, milestoneTitles, projectNode);
                issueNodeList.forEach((issueNode) -> {
                    Issue issue = util.makeIssueFromIssueNode(issueNode);
                    issue.setProject(currentProject);
                    util.putIssueToStoryIssueMap(storyTitles, storyIssuesMap, issue);
                });
            });

            PageInfo projectsPageInfo = currentProjects.getPageInfo();
            currentEndCursor = projectsPageInfo.getEndCursor();
            hasNextPage = projectsPageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get story issues list");
        return util.makeStoryIdIssuesMap(storyIssuesMap);
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

    private List<IssueNode> getSingleProjectIssueNodeList(String token, String projectFullPath,
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

        log.info("Get single project issue node list: " + projectFullPath);
        return issueNodeList;
    }

    public List<Project> getProjects(String token) {
        Set<Project> projects = new HashSet<>();

        String endCursor = gitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller.getProjectsResponse(token, endCursor)
                    .getData().getProjects();
            currentProjects.getNodes()
                    .forEach(projectNode -> projects.add(util.makeProjectFromProjectNode(projectNode)));

            PageInfo pageInfo = currentProjects.getPageInfo();
            endCursor = pageInfo.getEndCursor();
            hasNextPage = pageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get projects");
        return projects.stream().sorted().collect(Collectors.toList());
    }

    public List<String> getMilestoneTitles(String token, Set<String> projectIds) {
        if (projectIds == null || projectIds.size() == 0) return null;

        Set<String> milestoneTitles = new HashSet<>();
        String currentEndCursor = gitLabGraphQLCaller.getStartPagination();
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
        if (projectMilestones == null) return;
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
        if (groupMilestones == null) return;
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
        if (projectIds == null || projectIds.size() == 0) return null;

        Set<String> storyTitles = new HashSet<>();
        String currentEndCursor = gitLabGraphQLCaller.getStartPagination();
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

    public Issue updateStatus(String token, String issueId, String newStatusTitle, String projectFullPath) {
        IssueNode issueNode = gitLabGraphQLCaller.getIssueResponse(token, issueId).getData().getIssue();
        String issueIid = issueNode.getIid();
        String currentStatusLabelId = util.getStatusLabelId(issueNode);
        String newStatusLabelId;
        try {
            newStatusLabelId = getNewStatusLabelId(token, newStatusTitle, projectFullPath);
        } catch (NullPointerException e) {
            log.error("Could not find status with the title: " + newStatusTitle +
                    ", please check if this is really a valid status title!");
            return util.makeIssueFromIssueNode(issueNode);
        }

        if (!currentStatusLabelId.equals(newStatusLabelId)) {
            String currentStatusLabelIdNum = util.getIdNumValue(currentStatusLabelId);
            String newStatusLabelIdNum = util.getIdNumValue(newStatusLabelId);

            UpdateIssueDataResponse updateIssueDataResponse = gitLabGraphQLCaller.
                    updateStatusLabel(token, projectFullPath, issueIid, currentStatusLabelIdNum, newStatusLabelIdNum);

            log.info("Updated status: " + newStatusTitle + " of issue: " + issueId);
            return util.makeIssueFromIssueNode(updateIssueDataResponse.getData().getUpdateIssue().getIssue());
        }
        log.info("Failed to update status of issue: " + issueId);
        return util.makeIssueFromIssueNode(issueNode);
    }

    private String getNewStatusLabelId(String token, String newStatusTitle, String projectFullPath)
            throws NullPointerException {
        String newStatusLabelId;
        // Actually issues in "Backlog" have no status label.
        // Therefore if we want to move an issue to Backlog then we only need to remove the current status label.
        if (newStatusTitle.equals("Backlog") || newStatusTitle.equals("")) {
            newStatusLabelId = "";
        } else {
            newStatusLabelId = gitLabGraphQLCaller.
                    getProjectLabelResponse(token, projectFullPath, newStatusTitle)
                    .getData().getProject().getLabel().getId();
        }
        return newStatusLabelId;
    }

    public Issue updateAssignee(String token, String issueId, String newAssigneeId, String projectFullPath) {
        IssueNode issueNode = gitLabGraphQLCaller.getIssueResponse(token, issueId).getData().getIssue();
        String issueIid = issueNode.getIid();

        String assigneeUsername = "";
        if (!newAssigneeId.toLowerCase().equals("unassigned") && !newAssigneeId.equals("")) {
            assigneeUsername = gitLabGraphQLCaller.getUserResponse(token, newAssigneeId)
                    .getData().getUser().getUsername();
        }

        IssueSetAssigneesDataResponse issueSetAssigneesDataResponse = gitLabGraphQLCaller.
                updateAssignee(token, projectFullPath, issueIid, assigneeUsername);
        log.info("Set assignee: " + assigneeUsername + " to issue: " + issueId);
        return util.makeIssueFromIssueNode(issueSetAssigneesDataResponse.getData().getIssueSetAssignees().getIssue());
    }

}
