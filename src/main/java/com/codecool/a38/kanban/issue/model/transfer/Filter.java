package com.codecool.a38.kanban.issue.model.transfer;

import lombok.Data;

import java.util.Set;

@Data
public class Filter {

    private Set<String> projectIds;

    private Set<String> projectFullPaths;

    private Set<String> milestoneTitles;

    private Set<String> storyTitles;

}
