package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.changeAssigneeResponse.ChangeIssueAssigneeResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueCurrentStatus.IssueCurrentLabelsResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueCurrentStatus.NodesItem;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issueUpdateResponse.UpdateIssueResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.issuesIID.IssuesIIDResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.projectPathResponse.ProjectPathResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.issueMutations.statusIDresponse.StatusIDResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.milestones.MilestonesResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.projects.ProjectsResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.projectsIssues.ProjectsIssuesResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.stories.StoriesResponse;
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

    private RestTemplate restTemplate;

    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectsIssuesResponse getProjectsIssuesResponse(String token,
                                                            Set<String> projectIds, Set<String> milestoneTitles) {
        String query = "{\"query\":\"{\\n" +
                "projects(ids:" + getFormattedString(projectIds) + ") {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      issues(state: opened, milestoneTitle:" + getFormattedString(milestoneTitles) + ") {\\n" +
                "          nodes {\\n" +
                "              id\\n" +
                "              title\\n" +
                "              description\\n" +
                "              webUrl\\n" +
                "              dueDate\\n" +
                "              userNotesCount\\n" +
                "              reference\\n" +
                "              assignees(first: 1) {\\n" +
                "                  nodes {\\n" +
                "                      id\\n" +
                "                      name\\n" +
                "                      avatarUrl\\n" +
                "                  }\\n" +
                "              }\\n" +
                "              milestone {\\n" +
                "                  id\\n" +
                "                  title\\n" +
                "              }\\n" +
                "              labels {\\n" +
                "                  nodes {\\n" +
                "                      id\\n" +
                "                      title\\n" +
                "                      color\\n" +
                "                  }\\n" +
                "              }\\n" +
                "          }\\n" +
                "      }\\n" +
                "    }\\n" +
                "}\\n" +
                "}\",\"variables\":{}}";

        ResponseEntity<ProjectsIssuesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsIssuesResponse.class);
        log.info("Get projects issues response");
        return responseEntity.getBody();
    }

    public ProjectsResponse getProjectsResponse(String token) {
        String query = "{\"query\":\"{\\n" +
                "  projects {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      group {\\n" +
                "        id\\n" +
                "        name\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<ProjectsResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsResponse.class);
        log.info("Get projects response");
        return responseEntity.getBody();
    }

    public MilestonesResponse getMilestonesResponse(String token, Set<String> projectIds) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids:" + getFormattedString(projectIds) + ") {\\n" +
                "    nodes {\\n" +
                "      milestones {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<MilestonesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), MilestonesResponse.class);
        log.info("Get milestones response");
        return responseEntity.getBody();
    }

    public StoriesResponse getStoriesResponse(String token, Set<String> projectIds) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids:" + getFormattedString(projectIds) + ") {\\n" +
                "    nodes {\\n" +
                "      labels(searchTerm: \\\"Story: \\\") {\\n" +
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

        ResponseEntity<StoriesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), StoriesResponse.class);
        log.info("Get stories response");
        return responseEntity.getBody();
    }

    private String getFormattedString(Set<String> strings) {
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

    public void updateIssue(String token, String path, int id, int removableLabelId, int newLabelId) {
        String query = "{\"query\":\"mutation {\\n" +
                "  updateIssue(input: {projectPath: \\\"" + path + "\\\", iid: \\\"" + id + "\\\", addLabelIds: \\\"" + newLabelId + "\\\", removeLabelIds: \\\"" + removableLabelId + "\\\"}) {\\n" +
                "    issue {\\n" +
                "      labels {\\n" +
                "        nodes {\\n" +
                "          title\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<UpdateIssueResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), UpdateIssueResponse.class);
        log.info(responseEntity.getBody().getData().getUpdateIssue().getIssue().getLabels().getNodes().toString());
    }

    public void changeAssignee(String token, String assignee, String path, int issueIID) {
        String q = "{\"query\":\"mutation {\\n" +
                "  issueSetAssignees(input: {assigneeUsernames: \\\"" + assignee + "\\\", projectPath: \\\"" + path + "\\\", iid: \\\"" + issueIID + "\\\"}) {\\n" +
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
