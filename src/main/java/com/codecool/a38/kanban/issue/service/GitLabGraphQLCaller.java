package com.codecool.a38.kanban.issue.service;

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

import java.util.Set;

@Service
@Slf4j
public class GitLabGraphQLCaller {

    private static final String URL = "https://gitlab.techpm.guru/api/graphql";
    private static final String start = "[\\\"";
    private static final String delimiter = "\\\", \\\"";
    private static final String end = "\\\"]";

    private RestTemplate restTemplate;

    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectsIssuesResponse getProjectsIssuesResponse(String token,
                                                            Set<String> projectIds, Set<String> milestoneTitles) {
        String formattedProjectIds = start + String.join(delimiter, projectIds) + end;
        String formattedMilestoneTitles = start + String.join(delimiter, milestoneTitles) + end;

        String query = "{\"query\":\"{\\n" +
                "projects(ids:" + formattedProjectIds + ") {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      issues(state: opened, milestoneTitle:" + formattedMilestoneTitles + ") {\\n" +
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

        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
        ResponseEntity<ProjectsIssuesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, headers), ProjectsIssuesResponse.class);
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

        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
        ResponseEntity<ProjectsResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, headers), ProjectsResponse.class);
        log.info("Get projects response");
        return responseEntity.getBody();
    }

    public MilestonesResponse getMilestonesResponse(String token, Set<String> projectIds) {
        String formattedProjectIds = start + String.join(delimiter, projectIds) + end;
        String query = "{\"query\":\"{\\n" +
                "  projects(ids:" + formattedProjectIds + ") {\\n" +
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

        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
        ResponseEntity<MilestonesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, headers), MilestonesResponse.class);
        log.info("Get milestones response");
        return responseEntity.getBody();
    }

    public StoriesResponse getStoriesResponse(String token, Set<String> projectIds) {
        String formattedProjectIds = start + String.join(delimiter, projectIds) + end;
        String query = "{\"query\":\"{\\n" +
                "  projects(ids:" + formattedProjectIds + ") {\\n" +
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

        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
        ResponseEntity<StoriesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, headers), StoriesResponse.class);
        log.info("Get stories response");
        return responseEntity.getBody();
    }

}
