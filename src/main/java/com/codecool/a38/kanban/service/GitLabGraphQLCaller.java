package com.codecool.a38.kanban.service;

import com.codecool.a38.kanban.model.generated.ProjectData;
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

    public ProjectData getProjectData() {
        String url = "https://gitlab.com/api/graphql";
        String token = "VqPH7dptiz5GjSJEgZ6H";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        String query = "{\"query\":\"{\\n" +
                "projects(membership: true) {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      fullPath\\n" +
                "      webUrl\\n" +
                "      issues {\\n" +
                "          nodes {\\n" +
                "              id\\n" +
                "              title\\n" +
                "              description\\n" +
                "              webUrl\\n" +
                "              designCollection {\\n" +
                "                  project {\\n" +
                "                      name\\n" +
                "                  }\\n" +
                "              }\\n" +
                "              assignees {\\n" +
                "                  nodes {\\n" +
                "                      id\\n" +
                "                      name\\n" +
                "                      avatarUrl\\n" +
                "                      webUrl\\n" +
                "                  }\\n" +
                "              }\\n" +
                "              milestone {\\n" +
                "                  id\\n" +
                "                  title\\n" +
                "              }\\n" +
                "              labels {\\n" +
                "                  nodes {\\n" +
                "                      title\\n" +
                "                  }\\n" +
                "              }\\n" +
                "          }\\n" +
                "      }\\n" +
                "    }\\n" +
                "}\\n" +
                "}\",\"variables\":{}}";

        ResponseEntity<ProjectData> responseEntity = restTemplate.postForEntity(
                url, new HttpEntity<>(query, headers), ProjectData.class);

        log.info("Get all project data, the response: " + Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

}
