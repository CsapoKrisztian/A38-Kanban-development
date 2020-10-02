package com.codecool.a38.kanban.issue.model.transfer;

import lombok.Data;

import java.util.List;

@Data
public class Filter {

    private List<String> projectIds;

    private List<String> milestoneTitles;

    private List<String> storyTitles;

}
