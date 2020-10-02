package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectNode;
import com.codecool.a38.kanban.issue.model.graphQLResponse.ProjectsDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class GitLabGraphQLCaller {

    private RestTemplate restTemplate;
    private String token = "c7AzmAuSjtw52YSkpt8A";
    private String url = "https://gitlab.techpm.guru/api/graphql";

    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectsDataResponse getProjectsDataResponse() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        String query = "{\"query\":\"{\\n" +
                "projects {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      issues(state: opened, sort: LABEL_PRIORITY_ASC) {\\n" +
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
                url,
                new HttpEntity<>(query, headers),
                ProjectsDataResponse.class);

        log.info("Get all project data, the response: " + Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    public List<ProjectNode> getAllProjects() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String query = "{\"query\":\"{\\n" +
                "projects {\\n" +
                "    nodes {\\n" +
                "      id\\n" +
                "      name\\n" +
                "      issues(state: opened, sort: LABEL_PRIORITY_ASC) {\\n" +
                "          nodes {\\n" +
                "              id\\n" +
                "              title\\n" +
                "          }\\n" +
                "      }\\n" +
                "    }\\n" +
                "}\\n" +
                "}\"}";
        RequestEntity requestEntity = RequestEntity
                .post(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .headers(headers)
                .body(query);
        ResponseEntity<ProjectsDataResponse> responseEntity = restTemplate.postForEntity(
                url,
                requestEntity,
                ProjectsDataResponse.class);
        return responseEntity.getBody().getData().getProjects().getNodes();
    }

}
