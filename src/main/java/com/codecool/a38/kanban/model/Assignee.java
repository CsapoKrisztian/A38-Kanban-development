package com.codecool.a38.kanban.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignee {

    @Id
    private String assigneeId;

    private String name;

    private String webUrl;

    private String avatarUrl;

}
