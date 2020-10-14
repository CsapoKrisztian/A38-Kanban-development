package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.config.service.ConfigDataProvider;
import com.codecool.a38.kanban.issue.model.Issue;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.UpdateIssueRequestBody;
import com.codecool.a38.kanban.issue.model.graphQLResponse.*;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueCurrentStatus.NodesItem;
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

    private ConfigDataProvider configDataProvider;


    public List<String> getStatusTitles() {
        log.info("get status titles");
        return configDataProvider.getStatusTitles();
    }

    public List<AssigneeIssues> getAssigneeIssuesList(String token, Set<String> projectIds,
                                                      Set<String> milestoneTitles, Set<String> storyTitles) {
        if (projectIds == null || milestoneTitles == null || storyTitles == null) return null;

        Map<User, List<Issue>> assigneeIssuesMap = new HashMap<>();
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getProjectsIssuesResponse(token, projectIds, milestoneTitles, currentEndCursor)
                    .getData().getProjects();

            currentProjects.getNodes().forEach(projectNode -> {
                Project currentProject = createProjectFromProjectNode(projectNode);
                List<IssueNode> issueNodeList = getAllProjectsIssueNodes(token, milestoneTitles, projectNode);
                issueNodeList.forEach(issueNode -> {
                    Issue issue = createIssueFromIssueNode(issueNode);
                    issue.setProject(currentProject);

                    if (issue.getStatus() != null && issue.getStory() != null
                            && storyTitles.contains(issue.getStory().getTitle())) {
                        User assignee = issue.getAssignee();
                        if (!assigneeIssuesMap.containsKey(assignee)) {
                            assigneeIssuesMap.put(assignee, new ArrayList<>());
                        }
                        assigneeIssuesMap.get(assignee).add(issue);
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

    public List<StoryIssues> getStoryIssuesList(String token, Set<String> projectIds,
                                                Set<String> milestoneTitles, Set<String> storyTitles) {
        if (projectIds == null || milestoneTitles == null || storyTitles == null) return null;

        Map<Label, List<Issue>> storyIssuesMap = new HashMap<>();
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        boolean hasNextPage;
        do {
            Projects currentProjects = gitLabGraphQLCaller
                    .getProjectsIssuesResponse(token, projectIds, milestoneTitles, currentEndCursor)
                    .getData().getProjects();

            currentProjects.getNodes().forEach(projectNode -> {
                Project currentProject = createProjectFromProjectNode(projectNode);
                List<IssueNode> issueNodeList = getAllProjectsIssueNodes(token, milestoneTitles, projectNode);
                issueNodeList.forEach((issueNode) -> {
                    Issue issue = createIssueFromIssueNode(issueNode);
                    issue.setProject(currentProject);

                    Label story = issue.getStory();
                    if (issue.getStatus() != null && story != null
                            && storyTitles.contains(story.getTitle())) {
                        if (!storyIssuesMap.containsKey(story)) {
                            storyIssuesMap.put(story, new ArrayList<>());
                        }
                        storyIssuesMap.get(story).add(issue);
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
            if (label.getTitle().startsWith(configDataProvider.getStoryPrefix())) {
                label.setTitle(label.getTitle().substring(configDataProvider.getStoryPrefix().length()));
                thisIssue.setStory(label);
                return;
            }
            String priorityDisplayTitle = configDataProvider.getPriorityTitleDisplayMap().get(label.getTitle());
            if (priorityDisplayTitle != null) {
                label.setTitle(priorityDisplayTitle);
                thisIssue.setPriority(label);
                return;
            }
            String statusDisplayTitle = configDataProvider.getStatusTitleDisplayMap().get(label.getTitle());
            if (statusDisplayTitle != null) {
                label.setTitle(statusDisplayTitle);
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
            Projects currentProjects = gitLabGraphQLCaller.getProjectsResponse(token, endCursor)
                    .getData().getProjects();
            currentProjects.getNodes().forEach(projectNode -> projects.add(createProjectFromProjectNode(projectNode)));

            PageInfo pageInfo = currentProjects.getPageInfo();
            endCursor = pageInfo.getEndCursor();
            hasNextPage = pageInfo.isHasNextPage();
        } while (hasNextPage);

        log.info("Get projects");
        return projects;
    }

    public Set<String> getMilestoneTitles(String token, Set<String> projectIds) {
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
        return milestoneTitles;
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

    public Set<String> getStoryTitles(String token, Set<String> projectIds) {
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
        return storyTitles;
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

    public void updateIssue(String token, UpdateIssueRequestBody data) {
        List<NodesItem> issuesCurrentLabels = gitLabGraphQLCaller.getIssueCurrentStatus(token, data.getId());
        String currentStatusId = "";
        String path = gitLabGraphQLCaller.getProjectPath(data.getId(), token);
        int issueIID = gitLabGraphQLCaller.getIssuesIID(token, data.getId());
        int newLabelID = Integer.parseInt(gitLabGraphQLCaller.getStatusID(path, data.getNewLabel(), token).replaceAll("([A-z /]).", ""));

        for (NodesItem labelNode : issuesCurrentLabels) {
            if (configDataProvider.getStatusTitles().contains(labelNode.getTitle())) {
                currentStatusId = labelNode.getId();
            }
        }
        if (!currentStatusId.equals("")) {
            int removableLabelID = Integer.parseInt(currentStatusId.replaceAll("([A-z /]).", ""));
            if (removableLabelID != newLabelID) {
                gitLabGraphQLCaller.updateIssue(token, path, issueIID, removableLabelID, newLabelID);
            }
        }
    }

    public void changeAssignee(String token, String userID, String issueID) {
        String path = gitLabGraphQLCaller.getProjectPath(issueID, token);
        int issueIID = gitLabGraphQLCaller.getIssuesIID(token, issueID);

        gitLabGraphQLCaller.changeAssignee(token, userID, path, issueIID);
    }

    private Map<User, List<Issue>> organizeIssuesByAssignee(List<IssueNode> issueNodeList, Projects currentProjects, Map<User, List<Issue>> assigneeIssuesMap) {
        currentProjects.getNodes().forEach(projectNode -> {
            Project currentProject = createProjectFromProjectNode(projectNode);
            issueNodeList.forEach(issueNode -> {
                Issue issue = createIssueFromIssueNode(issueNode);
                issue.setProject(currentProject);

                if (issue.getStatus() != null) {
                    User assignee = issue.getAssignee();
                    if (!assigneeIssuesMap.containsKey(assignee)) {
                        assigneeIssuesMap.put(assignee, new ArrayList<>());
                    }
                    assigneeIssuesMap.get(assignee).add(issue);
                }
            });
        });
        return assigneeIssuesMap;
    }

    public List<AssigneeIssues> getAllIssuesFromProject(String token, Filter data) {
        Map<User, List<Issue>> assigneeIssuesMap = new HashMap<>();
        if (data.getMilestoneTitles().contains("Select milestone")) {
            data.getMilestoneTitles().remove("Select milestone");
        }
        List<IssueNode> issues = new ArrayList<>();
        boolean hasNextPage;
        String currentEndCursor = GitLabGraphQLCaller.getStartPagination();
        Projects currentProjects = new Projects();

        do {
            if (!data.getProjectIds().isEmpty()) {
                for (String id : data.getProjectIds()) {
                    if (!data.getMilestoneTitles().isEmpty() && data.getStoryTitles().isEmpty()) {
                        for (String milestoneTitle : data.getMilestoneTitles()) {
                            if (getMilestoneTitles(token, Set.of(id)).contains(milestoneTitle)) {
                                currentProjects = gitLabGraphQLCaller
                                        .getIssuesByProjectAndMilestoneTitle(token, id, milestoneTitle, currentEndCursor)
                                        .getData().getProjects();

                                currentProjects.getNodes()
                                        .forEach(projectNode -> {
                                            issues.addAll(projectNode.getIssues().getNodes());
                                        });
                            }
                        }
                    }
                    if (!data.getStoryTitles().isEmpty() && data.getMilestoneTitles().isEmpty()) {
                        Set<String> stories = getStoryTitles(token, Set.of(id));
                        for (String storyTitle : data.getStoryTitles()) {
                            if (stories.contains(storyTitle) && !(storyTitle == null)) {
                                currentProjects = gitLabGraphQLCaller
                                        .getIssuesByProjectAndStoryTitle(token, id, storyTitle, currentEndCursor)
                                        .getData().getProjects();

                                currentProjects.getNodes()
                                        .forEach(projectNode -> {
                                            issues.addAll(projectNode.getIssues().getNodes());
                                        });
                            }
                        }

                    }
                    if (!data.getStoryTitles().isEmpty() && !data.getMilestoneTitles().isEmpty()) {

                        for (String storyTitle : data.getStoryTitles()) {
                            currentProjects = gitLabGraphQLCaller
                                    .getIssuesByProjectMilestoneAndStroryTitle(token, id, storyTitle, data.getMilestoneTitles().toArray()[0].toString(), currentEndCursor)
                                    .getData().getProjects();

                            currentProjects.getNodes()
                                    .forEach(projectNode -> {
                                        issues.addAll(projectNode.getIssues().getNodes());
                                    });
                        }
                        //itt most pontosan mit akarunk kapni? ha story 2 vagy story 3 akkor is adja vissza a milestone szerint az issuekat?
                    }
                    if (data.getMilestoneTitles().isEmpty() && data.getStoryTitles().isEmpty()) {
                        currentProjects = gitLabGraphQLCaller
                                .getAllIssuesFromProject(token, id, currentEndCursor)
                                .getData().getProjects();

                        currentProjects.getNodes()
                                .forEach(projectNode -> {
                                    issues.addAll(projectNode.getIssues().getNodes());
                                });
                    }
                    organizeIssuesByAssignee(issues, currentProjects, assigneeIssuesMap);
                    issues.clear();
                    log.info("Returned organized issues list" + id);
                }
            }
            PageInfo projectsPageInfo = currentProjects.getPageInfo();
            currentEndCursor = projectsPageInfo.getEndCursor();
            hasNextPage = projectsPageInfo.isHasNextPage();
            log.info(String.valueOf(hasNextPage));

        } while (hasNextPage);


        return assigneeIssuesMap.entrySet().stream()
                .map(e -> AssigneeIssues.builder()
                        .assignee(e.getKey())
                        .issues(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}