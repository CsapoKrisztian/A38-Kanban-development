package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.config.service.ConfigDataProvider;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueData.IssueDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueSetAsignee.IssueSetAssigneesDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.updateIssueData.UpdateIssueDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.projectsData.ProjectsDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.singleGroupData.SingleGroupDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.singleProjectData.SingleProjectDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.userData.UserDataResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class GitLabGraphQLCaller {

    private GitlabGraphQLCallerUtil util;

    private RestTemplate restTemplate;

    private ConfigDataProvider configDataProvider;


    public String getStartPagination() {
        return util.getStartPagination();
    }


    public ProjectsDataResponse getProjectsIssuesResponse(String token, Set<String> projectIds,
                                                          Set<String> milestoneTitles, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids: " + util.getFormattedFilter(projectIds) + util.getFormattedPagination(endCursor) + ") {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      fullPath\\n" +
                "      name\\n" +
                "      group {\\n" +
                "        id\\n" +
                "        name\\n" +
                "      }\\n" +
                "      issues(state: opened " + util.getMilestoneFilter(milestoneTitles) + ") {\\n" +
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
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get projects issues response");
        return responseEntity.getBody();
    }

    public SingleProjectDataResponse getSingleProjectIssuesResponse(String token, String projectFullPath,
                                                                    Set<String> milestoneTitles, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectFullPath + "\\\") {\\n" +
                "    issues(state: opened " + util.getMilestoneFilter(milestoneTitles) + util.getFormattedPagination(endCursor) + ") {\\n" +
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
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), SingleProjectDataResponse.class);
        log.info("Get single project issues response: " + projectFullPath);
        return responseEntity.getBody();
    }

    public ProjectsDataResponse getProjectsResponse(String token, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects" + util.getFormattedPaginationWithBrackets(endCursor) + " {\\n" +
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
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get projects response");
        return responseEntity.getBody();
    }

    public ProjectsDataResponse getMilestonesResponse(String token, Set<String> projectIds, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids: " + util.getFormattedFilter(projectIds) + util.getFormattedPagination(endCursor) + ") {\\n" +
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
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get milestones response");
        return responseEntity.getBody();
    }

    public SingleProjectDataResponse getSingleProjectMilestonesResponse(String token, String projectFullPath,
                                                                        String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectFullPath + "\\\") {\\n" +
                "    milestones" + util.getFormattedPaginationWithBrackets(endCursor) + " {\\n" +
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
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), SingleProjectDataResponse.class);
        log.info("Get single project milestones response: " + projectFullPath);
        return responseEntity.getBody();
    }

    public SingleGroupDataResponse getSingleGroupMilestonesResponse(String token, String groupFullPath,
                                                                    String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  group(fullPath: \\\"" + groupFullPath + "\\\") {\\n" +
                "    milestones" + util.getFormattedPaginationWithBrackets(endCursor) + " {\\n" +
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
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), SingleGroupDataResponse.class);
        log.info("Get single group milestones response: " + groupFullPath);
        return responseEntity.getBody();
    }

    public ProjectsDataResponse getProjectsStoryLabelsResponse(String token, Set<String> projectIds, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids: " + util.getFormattedFilter(projectIds) + util.getFormattedPagination(endCursor) + ") {\\n" +
                "    nodes {\\n" +
                "      fullPath\\n" +
                "      labels(searchTerm: \\\"" + configDataProvider.getStoryPrefix() + "\\\") {\\n" +
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
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get stories response");
        return responseEntity.getBody();
    }

    public SingleProjectDataResponse getSingleProjectStoriesResponse(String token, String projectFullPath,
                                                                     String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectFullPath + "\\\") {\\n" +
                "    labels(searchTerm: \\\"" + configDataProvider.getStoryPrefix() + "\\\" " + util.getFormattedPagination(endCursor) + ") {\\n" +
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
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), SingleProjectDataResponse.class);
        log.info("Get single project stories response: " + projectFullPath);
        return responseEntity.getBody();
    }


    public IssueDataResponse getIssueResponse(String token, String issueId) {
        String query = "{\"query\":\"{\\n" +
                "  issue(id: \\\"" + issueId + "\\\") {\\n" +
                "    iid\\n" +
                "    labels {\\n" +
                "      nodes {\\n" +
                "        id\\n" +
                "        title\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<IssueDataResponse> responseEntity = restTemplate.postForEntity(
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), IssueDataResponse.class);
        log.info("Get issue data response: " + issueId);
        return responseEntity.getBody();
    }

    public SingleProjectDataResponse getProjectLabelResponse(String token, String projectPath, String labelTitle) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectPath + "\\\") {\\n" +
                "    label(title: \\\"" + labelTitle + "\\\") {\\n" +
                "      id\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n\",\"variables\":{}}";

        ResponseEntity<SingleProjectDataResponse> responseEntity = restTemplate.postForEntity(
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), SingleProjectDataResponse.class);
        log.info("Get label data response: " + labelTitle + " in project: " + projectPath);
        return responseEntity.getBody();
    }

    public UpdateIssueDataResponse updateStatusLabel(String token, String projectPath, String issueIid,
                                                     String removableLabelId, String addLabelId) {
        String query = "{\"query\":\"mutation {\\n" +
                "  updateIssue(input: {projectPath: \\\"" + projectPath + "\\\", iid: \\\"" + issueIid + "\\\", removeLabelIds: \\\"" + removableLabelId + "\\\", addLabelIds: \\\"" + addLabelId + "\\\"}) {\\n" +
                "    issue {\\n" +
                "      id\\n" +
                "      title\\n" +
                "      description\\n" +
                "      webUrl\\n" +
                "      dueDate\\n" +
                "      userNotesCount\\n" +
                "      reference\\n" +
                "      assignees {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          name\\n" +
                "          avatarUrl\\n" +
                "        }\\n" +
                "      }\\n" +
                "      milestone {\\n" +
                "        id\\n" +
                "        title\\n" +
                "      }\\n" +
                "      labels {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "          description\\n" +
                "          color\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "    errors\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<UpdateIssueDataResponse> responseEntity = restTemplate.postForEntity(
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), UpdateIssueDataResponse.class);
        log.info("Update issue, return update issue data response: " + issueIid);
        return responseEntity.getBody();
    }


    public UserDataResponse getUserResponse(String token, String userId) {
        String query = "{\"query\":\"{\\n" +
                "  user(id: \\\"" + userId + "\\\") {\\n" +
                "    username\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<UserDataResponse> responseEntity = restTemplate.postForEntity(
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), UserDataResponse.class);
        log.info("Get user data response: " + userId);
        return responseEntity.getBody();
    }

    public IssueSetAssigneesDataResponse updateAssignee(String token, String projectPath,
                                                        String issueIid, String assigneeUsername) {
        String query = "{\"query\":\"mutation {\\n" +
                "  issueSetAssignees(input: {projectPath: \\\"" + projectPath + "\\\", iid: \\\"" + issueIid + "\\\", assigneeUsernames: \\\"" + assigneeUsername + "\\\"}) {\\n" +
                "    issue {\\n" +
                "      id\\n" +
                "      title\\n" +
                "      description\\n" +
                "      webUrl\\n" +
                "      dueDate\\n" +
                "      userNotesCount\\n" +
                "      reference\\n" +
                "      assignees {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          name\\n" +
                "          avatarUrl\\n" +
                "        }\\n" +
                "      }\\n" +
                "      milestone {\\n" +
                "        id\\n" +
                "        title\\n" +
                "      }\\n" +
                "      labels {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "          color\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<IssueSetAssigneesDataResponse> responseEntity = restTemplate.postForEntity(
                util.getGraphQlApiUrl(), new HttpEntity<>(query, util.getHeaders(token)), IssueSetAssigneesDataResponse.class);
        log.info("Set assignee to issue, return issue set assignee data response: " + projectPath);
        return responseEntity.getBody();
    }

}
