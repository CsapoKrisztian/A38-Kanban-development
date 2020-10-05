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
    private static final String TOKEN = "JbHJ7hUuBpS3syCQn748";

    private static final HttpHeaders HEADERS = new HttpHeaders() {{
        add("Authorization", "Bearer " + TOKEN);
        add("Content-Type", "application/json");
    }};

    private RestTemplate restTemplate;

    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String start = "[\\\"";
    private static final String delimiter = "\\\", \\\"";
    private static final String end = "\\\"]";

    public ProjectsIssuesResponse getProjectsIssuesResponse(Set<String> projectIds, Set<String> milestoneTitles) {
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

        ResponseEntity<ProjectsIssuesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, HEADERS), ProjectsIssuesResponse.class);

        log.info("Get projects issues response");
        return responseEntity.getBody();
    }

    public ProjectsResponse getProjectsResponse() {
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
                URL, new HttpEntity<>(query, HEADERS), ProjectsResponse.class);

        log.info("Get projects response");
        return responseEntity.getBody();
    }

    public MilestonesResponse getMilestonesResponse(Set<String> projectIds) {
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

        ResponseEntity<MilestonesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, HEADERS), MilestonesResponse.class);

        log.info("Get milestones response");
        return responseEntity.getBody();
    }

    public StoriesResponse getStoriesResponse(Set<String> projectIds) {
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

        ResponseEntity<StoriesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, HEADERS), StoriesResponse.class);

        log.info("Get stories response");
        return responseEntity.getBody();
    }

}
