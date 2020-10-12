package com.codecool.a38.kanban.issue.model.requestBodies;

import lombok.Data;

import java.util.List;

@Data
public class GetAllIssuesRequestBody {

    List<String> projectIDs;
    List<String> milestoneTitles;
    List<String> storyTitles;

}
