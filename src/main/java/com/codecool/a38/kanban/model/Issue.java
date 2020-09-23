package com.codecool.a38.kanban.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Issue {

    @Id
    private String issueId;

    private String issueTitle;

    private String issueDescription;

    private String projectName;

    private String mileStoneName;

    private String issueUrl;

    @ElementCollection
    private List<String> labels;

    @ManyToOne
    private Assignee assignee;

}

