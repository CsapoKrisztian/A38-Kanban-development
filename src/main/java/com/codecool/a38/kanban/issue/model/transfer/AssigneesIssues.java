package com.codecool.a38.kanban.issue.model.transfer;

import com.codecool.a38.kanban.issue.model.Assignee;
import com.codecool.a38.kanban.issue.model.Issue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssigneesIssues {

    private Assignee assignee;

    private List<Issue> issues;

}
