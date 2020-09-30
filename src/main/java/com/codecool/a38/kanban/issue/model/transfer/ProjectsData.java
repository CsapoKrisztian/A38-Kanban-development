package com.codecool.a38.kanban.issue.model.transfer;

import com.codecool.a38.kanban.issue.model.MileStone;
import com.codecool.a38.kanban.issue.model.Project;
import com.codecool.a38.kanban.issue.model.Story;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProjectsData {

     private AssigneesIssues assigneesIssues;

     private StoriesIssues storiesIssues;

     private Set<Project> projects = new HashSet<>();

     private Set<MileStone> mileStones = new HashSet<>();

     private Set<Story> stories = new HashSet<>();

}
