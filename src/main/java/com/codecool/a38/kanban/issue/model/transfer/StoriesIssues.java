package com.codecool.a38.kanban.issue.model.transfer;

import com.codecool.a38.kanban.issue.model.Issue;
import com.codecool.a38.kanban.issue.model.Story;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoriesIssues {

    private Story story;

    private List<Issue> issues;

}
