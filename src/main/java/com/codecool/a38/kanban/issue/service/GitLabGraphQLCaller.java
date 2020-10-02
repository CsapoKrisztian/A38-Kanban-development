package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectsIssuesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class GitLabGraphQLCaller {

    private static final String URL = "https://gitlab.techpm.guru/api/graphql";

    private static final String TOKEN = "JbHJ7hUuBpS3syCQn748";

    private static final HttpHeaders HEADERS = new HttpHeaders() {{
        add("Authorization", "Bearer " + TOKEN);
        add("Content-Type", "application/json");
    }};

    private RestTemplate restTemplate;

    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectsIssuesResponse getProjectsIssuesResponse(List<String> projectIds, List<String> milestoneTitles) {
        String start = "[\\\"";
        String delimiter = "\\\", \\\"";
        String end = "\\\"]";
        String formattedProjectIds =  start + String.join(delimiter, projectIds) + end;
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

        ResponseEntity<ProjectsIssuesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, HEADERS), ProjectsIssuesResponse.class);

        log.info("Get ProjectsIssuesResponse: " + Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

}
