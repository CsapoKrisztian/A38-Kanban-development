package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.generated.ProjectsDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class GitLabGraphQLCaller {

    private RestTemplate restTemplate;
    String token = "JbHJ7hUuBpS3syCQn748";
    HttpHeaders headers = new HttpHeaders();


    public GitLabGraphQLCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectsDataResponse getProjectData() {
        String url = "https://gitlab.techpm.guru/api/graphql";

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
