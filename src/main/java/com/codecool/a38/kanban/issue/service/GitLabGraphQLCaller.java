package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.generated.ProjectsDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
public class GitLabGraphQLCaller {

    private RestTemplate restTemplate;

    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectsDataResponse getProjectData() {
        String url = "https://gitlab.techpm.guru/api/graphql";
        String token = "JbHJ7hUuBpS3syCQn748";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        String query = "{\"query\":\"{\\n" +
                "projects {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      issues(state: opened) {\\n" +
                "          nodes {\\n" +
                "              id\\n" +
                "              title\\n" +
                "              description\\n" +
                "              webUrl\\n" +
                "              dueDate\\n" +
                "              userNotesCount\\n" +
                "              reference\\n" +
                "              assignees {\\n" +
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
                url, new HttpEntity<>(query, headers), ProjectsDataResponse.class);

        log.info("Get all project data, the response: " + Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

}
