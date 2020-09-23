package com.codecool.a38.kanban.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class GitLabController {

    private final String url = "https://gitlab.com/api/graphql";
    private final String token = "VqPH7dptiz5GjSJEgZ6H";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/projects")
    public Object getProjectsData() {
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

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
                url, new HttpEntity<>(query, headers), Object.class);

        log.info("Get all projects, the response: " + responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

}
