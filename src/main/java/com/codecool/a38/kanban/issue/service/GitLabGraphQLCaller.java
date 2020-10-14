package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.changeAssigneeResponse.ChangeIssueAssigneeResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueCurrentStatus.IssueCurrentLabelsResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueCurrentStatus.NodesItem;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueUpdateResponse.UpdateIssueResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issuesIID.IssuesIIDResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.projectPathResponse.ProjectPathResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.statusIDresponse.StatusIDResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.projectsData.ProjectsDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.singleGroupData.SingleGroupDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.singleProjectData.SingleProjectDataResponse;
import com.codecool.a38.kanban.issue.model.usernameByUserID.UsernameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class GitLabGraphQLCaller {

    private static final String URL = "https://gitlab.techpm.guru/api/graphql";

    private static final String startPagination = "start";

    public static String getStartPagination() {
        return startPagination;
    }

    private RestTemplate restTemplate;

    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ProjectsDataResponse getProjectsIssuesResponse(String token, Set<String> projectIds,
                                                          Set<String> milestoneTitles, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids: " + getFormattedFilter(projectIds) + getFormattedPagination(endCursor) + ") {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      fullPath\\n" +
                "      name\\n" +
                "      group {\\n" +
                "        id\\n" +
                "        name\\n" +
                "      }\\n" +
                "      issues(state: opened, milestoneTitle: " + getFormattedFilter(milestoneTitles) + ") {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "          description\\n" +
                "          webUrl\\n" +
                "          dueDate\\n" +
                "          userNotesCount\\n" +
                "          reference\\n" +
                "          assignees {\\n" +
                "            nodes {\\n" +
                "              id\\n" +
                "              name\\n" +
                "              avatarUrl\\n" +
                "            }\\n" +
                "          }\\n" +
                "          milestone {\\n" +
                "            id\\n" +
                "            title\\n" +
                "          }\\n" +
                "          labels {\\n" +
                "            nodes {\\n" +
                "              id\\n" +
                "              title\\n" +
                "              description\\n" +
                "              color\\n" +
                "            }\\n" +
                "          }\\n" +
                "        }\\n" +
                "        pageInfo {\\n" +
                "          hasNextPage\\n" +
                "          endCursor\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "    pageInfo {\\n" +
                "      hasNextPage\\n" +
                "      endCursor\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<ProjectsDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get projects issues response");
        return responseEntity.getBody();
    }

    public SingleProjectDataResponse getSingleProjectIssuesResponse(String token, String projectFullPath,
                                                                    Set<String> milestoneTitles, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectFullPath + "\\\") {\\n" +
                "    issues(state: opened, milestoneTitle: " + getFormattedFilter(milestoneTitles) + getFormattedPagination(endCursor) + ") {\\n" +
                "      nodes {\\n" +
                "        id\\n" +
                "        title\\n" +
                "        description\\n" +
                "        webUrl\\n" +
                "        dueDate\\n" +
                "        userNotesCount\\n" +
                "        reference\\n" +
                "        assignees(first: 1) {\\n" +
                "          nodes {\\n" +
                "            id\\n" +
                "            name\\n" +
                "            avatarUrl\\n" +
                "          }\\n" +
                "        }\\n" +
                "        milestone {\\n" +
                "          id\\n" +
                "          title\\n" +
                "        }\\n" +
                "        labels {\\n" +
                "          nodes {\\n" +
                "            id\\n" +
                "            title\\n" +
                "            description\\n" +
                "            color\\n" +
                "          }\\n" +
                "        }\\n" +
                "      }\\n" +
                "      pageInfo {\\n" +
                "        hasNextPage\\n" +
                "        endCursor\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<SingleProjectDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), SingleProjectDataResponse.class);
        log.info("Get single project issues response: " + projectFullPath);
        return responseEntity.getBody();
    }

    public ProjectsDataResponse getProjectsResponse(String token, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects" + getFormattedPaginationWithBrackets(endCursor) + " {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      fullPath\\n" +
                "      name\\n" +
                "      group {\\n" +
                "        id\\n" +
                "        name\\n" +
                "      }\\n" +
                "    }\\n" +
                "    pageInfo {\\n" +
                "      hasNextPage\\n" +
                "      endCursor\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<ProjectsDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get projects response");
        return responseEntity.getBody();
    }

    public ProjectsDataResponse getMilestonesResponse(String token, Set<String> projectIds, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids: " + getFormattedFilter(projectIds) + getFormattedPagination(endCursor) + ") {\\n" +
                "    nodes {\\n" +
                "      fullPath\\n" +
                "      milestones {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "        }\\n" +
                "        pageInfo {\\n" +
                "          hasNextPage\\n" +
                "          endCursor\\n" +
                "        }\\n" +
                "      }\\n" +
                "      group {\\n" +
                "        fullPath\\n" +
                "        milestones {\\n" +
                "          nodes {\\n" +
                "            id\\n" +
                "            title\\n" +
                "          }\\n" +
                "          pageInfo {\\n" +
                "            hasNextPage\\n" +
                "            endCursor\\n" +
                "          }\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "    pageInfo {\\n" +
                "      hasNextPage\\n" +
                "      endCursor\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<ProjectsDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get milestones response");
        return responseEntity.getBody();
    }

    public SingleProjectDataResponse getSingleProjectMilestonesResponse(String token, String projectFullPath,
                                                                        String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectFullPath + "\\\") {\\n" +
                "    milestones" + getFormattedPaginationWithBrackets(endCursor) + " {\\n" +
                "      nodes {\\n" +
                "        id\\n" +
                "        title\\n" +
                "      }\\n" +
                "      pageInfo {\\n" +
                "        hasNextPage\\n" +
                "        endCursor\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<SingleProjectDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), SingleProjectDataResponse.class);
        log.info("Get single project milestones response: " + projectFullPath);
        return responseEntity.getBody();
    }

    public SingleGroupDataResponse getSingleGroupMilestonesResponse(String token, String groupFullPath,
                                                                    String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  group(fullPath: \\\"" + groupFullPath + "\\\") {\\n" +
                "    milestones" + getFormattedPaginationWithBrackets(endCursor) + " {\\n" +
                "      nodes {\\n" +
                "        id\\n" +
                "        title\\n" +
                "      }\\n" +
                "      pageInfo {\\n" +
                "        hasNextPage\\n" +
                "        endCursor\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<SingleGroupDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), SingleGroupDataResponse.class);
        log.info("Get single group milestones response: " + groupFullPath);
        return responseEntity.getBody();
    }

    public ProjectsDataResponse getProjectsStoryLabelsResponse(String token, Set<String> projectIds, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids: " + getFormattedFilter(projectIds) + getFormattedPagination(endCursor) + ") {\\n" +
                "    nodes {\\n" +
                "      fullPath\\n" +
                "      labels(searchTerm: \\\"Story: \\\") {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "          color\\n" +
                "        }\\n" +
                "       pageInfo {\\n" +
                "         hasNextPage\\n" +
                "         endCursor\\n" +
                "       }\\n" +
                "      }\\n" +
                "    }\\n" +
                "    pageInfo {\\n" +
                "      hasNextPage\\n" +
                "      endCursor\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<ProjectsDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get stories response");
        return responseEntity.getBody();
    }

    public SingleProjectDataResponse getSingleProjectStoriesResponse(String token, String projectFullPath,
                                                                     String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectFullPath + "\\\") {\\n" +
                "    labels(searchTerm: \\\"Story: \\\"" + getFormattedPagination(endCursor) + ") {\\n" +
                "      nodes {\\n" +
                "        id\\n" +
                "        title\\n" +
                "        color\\n" +
                "      }\\n" +
                "      pageInfo {\\n" +
                "        hasNextPage\\n" +
                "        endCursor\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<SingleProjectDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), SingleProjectDataResponse.class);
        log.info("Get single project stories response: " + projectFullPath);
        return responseEntity.getBody();
    }

    private String getFormattedPagination(String cursor) {
        return !cursor.equals(startPagination) ? getFormattedAfter(cursor) : "";
    }

    private String getFormattedPaginationWithBrackets(String cursor) {
        return !cursor.equals(startPagination) ? "(" + getFormattedAfter(cursor) + ")" : "";
    }

    private String getFormattedAfter(String cursor) {
        String before = "after: \\\"";
        String after = "\\\"";
        return before + cursor + after;
    }

    private String getFormattedFilter(Set<String> strings) {
        String before = "[\\\"";
        String delimiter = "\\\", \\\"";
        String after = "\\\"]";
        return before + String.join(delimiter, strings) + after;
    }

    private HttpHeaders getHeaders(String token) {
        return new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
    }

    public String getProjectPath(String issueId, String token) {
        String query = "{\"query\":\"{\\n" +
                "  issue(id: \\\"" + issueId + "\\\") {\\n" +
                "    iid\\n" +
                "    designCollection {\\n" +
                "      " +
                "project {\\n" +
                "        " +
                "fullPath\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\",\"variables\":{}}";
        ResponseEntity<ProjectPathResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectPathResponse.class);
        return responseEntity.getBody().getData().getIssue().getDesignCollection().getProject().getFullPath();
    }

    public String getStatusID(String path, String labelTitle, String token) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + path + "\\\") {\\n" +
                "    label(title: \\\"" + labelTitle + "\\\") {\\n" +
                "      id\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\",\"variables\":{}}";
        ResponseEntity<StatusIDResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), StatusIDResponse.class);
        return responseEntity.getBody().getData().getProject().getLabel().getId();
    }

    public List<NodesItem> getIssueCurrentStatus(String token, String issueID) {
        String query = "{\"query\":\"{\\n" +
                "    issue(id:\\\"" + issueID + "\\\"){\\n" +
                "      labels{\\n" +
                "        nodes{\\n" +
                "          id\\n" +
                "          title\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "" +
                "\",\"variables\":{}}";
        ResponseEntity<IssueCurrentLabelsResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), IssueCurrentLabelsResponse.class);
        return responseEntity.getBody().getData().getIssue().getLabels().getNodes();
    }

    public Integer getIssuesIID(String token, String issueID) {
        String query = "{\"query\":\"{\\n" +
                "  issue(id: \\\"" + issueID + "\\\") {\\n" +
                "    iid\\n" +
                "  }\\n" +
                "}\",\"variables\":{}}";
        ResponseEntity<IssuesIIDResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), IssuesIIDResponse.class);
        return Integer.parseInt(responseEntity.getBody().getData().getIssue().getIid());
    }

    public void updateIssue(String token, String projectPath, int issueIid, int removableLabelId, int addLabelId) {
        String query = "{\"query\":\"mutation {\\n" +
                "  updateIssue(input: {projectPath: \\\"" + projectPath + "\\\", iid: \\\"" + issueIid + "\\\", removeLabelIds: \\\"" + removableLabelId + "\\\", addLabelIds: \\\"" + addLabelId + "\\\"}) {\\n" +
                "    issue {\\n" +
                "      id\\n" +
                "      title\\n" +
                "      labels {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "          color\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "    errors\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<UpdateIssueResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), UpdateIssueResponse.class);
        log.info(responseEntity.getBody().getData().getUpdateIssue().getIssue().getLabels().getNodes().toString());
    }

    private String getUsernameByUserID(String token, String userID) {
        String query = "{\"query\":\"{\\n" +
                "  user(id:\\\"" + userID + "\\\"){\\n" +
                "    username\\n" +
                "  }\\n}\\n\",\"variables\":{}}";

        ResponseEntity<UsernameResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), UsernameResponse.class);
        return responseEntity.getBody().getData().getUser().getUsername();

    }

    public void changeAssignee(String token, String userID, String path, int issueIID) {
        String username;
        if (userID == "unassigned" || userID == null || userID == "") {
            username = "";
        } else {
            username = getUsernameByUserID(token, userID);
        }

        String q = "{\"query\":\"mutation {\\n" +
                "  issueSetAssignees(input: {assigneeUsernames: \\\"" + username + "\\\", projectPath: \\\"" + path + "\\\", iid: \\\"" + issueIID + "\\\"}) {\\n" +
                "    issue {\\n" +
                "      title\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<ChangeIssueAssigneeResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(q, getHeaders(token)), ChangeIssueAssigneeResponse.class);
        log.info(responseEntity.getBody().getData().getIssueSetAssignees().getIssue().getTitle());
    }
}
