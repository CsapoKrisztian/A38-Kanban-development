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

    private static final String url = "https://gitlab.techpm.guru/api/graphql";

    private RestTemplate restTemplate;

    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getFormattedString(Set<String> strings) {
        String before = "[\\\"";
        String delimiter = "\\\", \\\"";
        String after = "\\\"]";
        return before + String.join(delimiter, strings) + after;
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

        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
        ResponseEntity<ProjectsIssuesResponse> responseEntity = restTemplate.postForEntity(
                url, new HttpEntity<>(query, headers), ProjectsIssuesResponse.class);
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
                url, new HttpEntity<>(query, headers), ProjectsResponse.class);
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

        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
        ResponseEntity<MilestonesResponse> responseEntity = restTemplate.postForEntity(
                url, new HttpEntity<>(query, headers), MilestonesResponse.class);
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

        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
        ResponseEntity<StoriesResponse> responseEntity = restTemplate.postForEntity(
                url, new HttpEntity<>(query, headers), StoriesResponse.class);
        log.info("Get stories response");
        return responseEntity.getBody();
    }

}
