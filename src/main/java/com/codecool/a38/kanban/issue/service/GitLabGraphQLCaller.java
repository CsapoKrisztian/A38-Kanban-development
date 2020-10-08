package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.graphQLResponse.projectsData.ProjectsDataResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.singleProjectData.SingleProjectDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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


    public SingleProjectDataResponse getSingleProjectIssuesResponse(String token, String projectFullPath,
                                                                    Set<String> milestoneTitles, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectFullPath + "\\\") {\\n" +
                "    issues(state: opened, milestoneTitle: " + getFormattedString(milestoneTitles) + getPagination(endCursor) + ") {\\n" +
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

        public ProjectsDataResponse getProjectsIssuesResponse(String token,
                                                            Set<String> projectIds, Set<String> milestoneTitles) {
        String query = "{\"query\":\"{\\n" +
                "projects(ids:" + getFormattedString(projectIds) + ") {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      fullPath\\n" +
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

        ResponseEntity<ProjectsDataResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsDataResponse.class);
        log.info("Get projects issues response");
        return responseEntity.getBody();
    }

    public ProjectsDataResponse getProjectsResponse(String token, String endCursor) {
        String pagination =  !endCursor.equals(startPagination) ? "(after: \\\"" + endCursor + "\\\")" : "";
        String query = "{\"query\":\"{\\n" +
                "  projects" + pagination + " {\\n" +
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
                "  projects(ids:" + getFormattedString(projectIds) + getPagination(endCursor) + ") {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      milestones {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "        }\\n" +
                "      }\\n" +
                "      group {\\n" +
                "        milestones {\\n" +
                "          nodes {\\n" +
                "            id\\n" +
                "            title\\n" +
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

    public ProjectsDataResponse getProjectsStoriesResponse(String token, Set<String> projectIds, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  projects(ids:" + getFormattedString(projectIds) + getPagination(endCursor) + ") {\\n" +
                "    nodes {\\n" +
                "      fullPath\\n" +
                "      labels(searchTerm: \\\"Story: \\\") {\\n" +
                "        nodes {\\n" +
                "          id\\n" +
                "          title\\n" +
                "          color\\n" +
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
        log.info("Get stories response");
        return responseEntity.getBody();
    }

    public SingleProjectDataResponse getSingleProjectStories(String token, String projectFullPath, String endCursor) {
        String query = "{\"query\":\"{\\n" +
                "  project(fullPath: \\\"" + projectFullPath + "\\\") {\\n" +
                "    labels(searchTerm: \\\"Story: \\\"" + getPagination(endCursor) + ") {\\n" +
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

    private String getPagination(String endCursor) {
        return !endCursor.equals(startPagination) ? "after: \\\"" + endCursor + "\\\"" : "";
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

}
