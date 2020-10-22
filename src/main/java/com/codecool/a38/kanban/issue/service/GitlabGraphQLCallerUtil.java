package com.codecool.a38.kanban.issue.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GitlabGraphQLCallerUtil {

    @Value("${gitlabServer.url}")
    private String gitlabServerUrl;


    private final String startPagination = "start pagination";

    public String getStartPagination() {
        return startPagination;
    }


    public String getFormattedPagination(String cursor) {
        return !cursor.equals(startPagination) ? getFormattedAfter(cursor) : "";
    }

    public String getFormattedPaginationWithBrackets(String cursor) {
        return !cursor.equals(startPagination) ? "(" + getFormattedAfter(cursor) + ")" : "";
    }

    private String getFormattedAfter(String cursor) {
        String before = "after: \\\"";
        String after = "\\\"";
        return before + cursor + after;
    }

    public String getMilestoneFilter(Set<String> milestoneTitles) {
        if (milestoneTitles == null) return "";
        String defaultOptionText = "Select milestone";
        milestoneTitles.remove(defaultOptionText);
        return milestoneTitles.size() != 0 ?
                "milestoneTitle: " + getFormattedFilter(milestoneTitles) : "";
    }

    public String getFormattedFilter(Set<String> strings) {
        String before = "[\\\"";
        String delimiter = "\\\", \\\"";
        String after = "\\\"]";
        return before + String.join(delimiter, strings) + after;
    }

    public String getGraphQlApiUrl() {
        return gitlabServerUrl + "/api/graphql";
    }

    public HttpHeaders getHeaders(String token) {
        return new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
            add("Content-Type", "application/json");
        }};
    }

}
