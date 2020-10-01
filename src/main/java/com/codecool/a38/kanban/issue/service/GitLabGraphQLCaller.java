package com.codecool.a38.kanban.issue.service;

import com.codecool.a38.kanban.issue.model.graphQLResponse.projectsIssues.ProjectsIssuesResponse;
import com.codecool.a38.kanban.issue.model.graphQLResponse.userIssues.AssigneeIssuesResponse;
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

    public ProjectsIssuesResponse getProjectsDataResponse() {
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

        ResponseEntity<ProjectsIssuesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, HEADERS), ProjectsIssuesResponse.class);

        log.info("Get ProjectsIssuesResponse: " + Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    public AssigneeIssuesResponse getAssigneeIssues(String userId) {
        String userIdNum = userId.substring("gid://gitlab/User/".length());
        String query = "{\"query\":\"{\\n" +
                "  user(id: \\\"" + userId + "\\\") {\\n" +
                "    id\\n" +
                "    name\\n" +
                "    avatarUrl\\n" +
                "    groupMemberships {\\n" +
                "      nodes {\\n" +
                "        group {\\n" +
                "          projects {\\n" +
                "            nodes {\\n" +
                "              id\\n" +
                "              name\\n" +
                "              issues(state: opened, assigneeId: \\\"" + userIdNum + "\\\") {\\n" +
                "                nodes {\\n" +
                "                  id\\n" +
                "                  title\\n" +
                "                  description\\n" +
                "                  webUrl\\n" +
                "                  dueDate\\n" +
                "                  userNotesCount\\n" +
                "                  reference\\n" +
                "                  assignees {\\n" +
                "                    nodes {\\n" +
                "                      id\\n" +
                "                      name\\n" +
                "                      avatarUrl\\n" +
                "                    }\\n" +
                "                  }\\n" +
                "                  milestone {\\n" +
                "                    id\\n" +
                "                    title\\n" +
                "                  }\\n" +
                "                  labels {\\n" +
                "                    nodes {\\n" +
                "                      id\\n" +
                "                      title\\n" +
                "                      color\\n" +
                "                    }\\n" +
                "                  }\\n" +
                "                }\\n" +
                "              }\\n" +
                "            }\\n" +
                "          }\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "    projectMemberships {\\n" +
                "      nodes {\\n" +
                "        project {\\n" +
                "          id\\n" +
                "          name\\n" +
                "          issues(state: opened, assigneeId: \\\"" + userId + "\\\") {\\n" +
                "            nodes {\\n" +
                "              id\\n" +
                "              title\\n" +
                "              description\\n" +
                "              webUrl\\n" +
                "              dueDate\\n" +
                "              userNotesCount\\n" +
                "              reference\\n" +
                "              assignees {\\n" +
                "                nodes {\\n" +
                "                  id\\n" +
                "                  name\\n" +
                "                  avatarUrl\\n" +
                "                }\\n" +
                "              }\\n" +
                "              milestone {\\n" +
                "                id\\n" +
                "                title\\n" +
                "              }\\n" +
                "              labels {\\n" +
                "                nodes {\\n" +
                "                  id\\n" +
                "                  title\\n" +
                "                  color\\n" +
                "                }\\n" +
                "              }\\n" +
                "            }\\n" +
                "          }\\n" +
                "        }\\n" +
                "      }\\n" +
                "    }\\n" +
                "  }\\n" +
                "}\\n" +
                "\",\"variables\":{}}";

        ResponseEntity<AssigneeIssuesResponse> responseEntity = restTemplate.postForEntity(
                URL, new HttpEntity<>(query, HEADERS), AssigneeIssuesResponse.class);

        log.info("Get UserIssuesResponse: " +
                Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toString()));
        return responseEntity.getBody();
    }

}
