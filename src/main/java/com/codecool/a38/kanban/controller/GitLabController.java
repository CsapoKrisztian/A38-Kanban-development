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
    public String getProjects() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        String query1 = "{\"query\":\"{\\nprojects(membership: true) {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      fullPath\\n" +
                "      issues {\\n" +
                "          nodes {\\n" +
                "              id\\n" +
                "              title\\n" +
                "              milestone {\\n" +
                "                  id\\n" +
                "                  title\\n" +
                "              }\\n" +
                "              labels {\\n" +
                "                  nodes {\\n" +
                "                      title\\n" +
                "                  }\\n" +
                "              }\\n" +
                "              \\n" +
                "          }\\n" +
                "      }\\n" +
                "    }\\n" +
                "}\\n}\",\"variables\":{}}";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,
                new HttpEntity<>(query1, headers), String.class);

        log.info("Get all projects, the response: " + responseEntity.getBody());
        return responseEntity.getBody();
    }

}
