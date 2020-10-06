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

import java.util.Objects;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
public class GitLabGraphQLCaller {

    private static final String URL = "https://gitlab.techpm.guru/api/graphql";

    private RestTemplate restTemplate;
    String token = "JbHJ7hUuBpS3syCQn748";
    HttpHeaders headers = new HttpHeaders();


    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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

        ResponseEntity<ProjectsIssuesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsIssuesResponse.class);
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

        ResponseEntity<ProjectsResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), ProjectsResponse.class);
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

        ResponseEntity<MilestonesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), MilestonesResponse.class);
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

        ResponseEntity<StoriesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, getHeaders(token)), StoriesResponse.class);
        log.info("Get stories response");
        return responseEntity.getBody();
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

    public String getProjectID(String path) {
        headers.add("Authorization", "Bearer " + token);
        String url = "https://gitlab.techpm.guru/api/graphql";
        String query = "{\n" +
                "  project(fullPath: " + path + ") {\n" +
                "    id\n" +
                "  }\n" +
                "}";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(query, headers), String.class);
        return responseEntity.getBody();
    }

    public String getMilestones(String path) {
        headers.add("Authorization", "Bearer " + token);
        String url = "https://gitlab.techpm.guru/api/graphql";
        String query = "{\n" +
                "  project(fullPath: " + path + ") {\n" +
                "    milestones{\n" +
                "      nodes{\n" +
                "        id\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(query, headers), String.class);
        return responseEntity.getBody();
    }

    public String getAssignees(String path) {
        headers.add("Authorization", "Bearer " + token);
        String url = "https://gitlab.techpm.guru/api/graphql";
        String query = "{\n" +
                "  project(fullPath: " + path + ") {\n" +
                "    projectMembers{\n" +
                "      nodes{\n" +
                "        id\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(query, headers), String.class);
        return responseEntity.getBody();
    }

    public void createThousandIssues(String path, String title) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        String projectID = getProjectID(path);
        int date = 1;
        String milestones = getMilestones(path);
        String assignees = getAssignees(path);
        String[] stories = {
                "Story: Story 1",
                "Story: Story 2",
                "Story: Story 3"};

        String[] labels = {"Backlog", "Dev review", "Development", "Documentation", "Final review", "Todo"};
        String[] prio = {"Priority: P0", "Priority: P1", "Priority: P2"};

        int randomLabel = new Random().nextInt(labels.length);
        int randomPrio = new Random().nextInt(prio.length);
        //int randomMilestone = new Random().nextInt(milestones.length);
        int randomStory = new Random().nextInt(stories.length);
        //int randomAssignee = new Random().nextInt(assignees.length);
        if (date + 1 < 30) {
            date++;
        } else {
            date = 1;
        }
        String url = "https://gitlab.techpm.guru/api/v4/projects/" + projectID + "/issues?" +
                "title=New issue " + title +
                "&labels=" + labels[randomLabel] + "," + stories[randomStory] +
                "&due_date=November " + date + ", 2020"
                //+"&assignee_ids=" + assignees[randomAssignee] + "," + assignees[randomAssignee] + "," + assignees[randomAssignee] +
                // "&milestone_id=" + milestones[randomMilestone]
                ;


        HttpEntity request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, request, String.class);

    }

}
