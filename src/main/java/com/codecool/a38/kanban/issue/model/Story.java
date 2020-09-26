package com.codecool.a38.kanban.issue.model;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@SuperBuilder
@NoArgsConstructor
public class Story extends Label {
}
