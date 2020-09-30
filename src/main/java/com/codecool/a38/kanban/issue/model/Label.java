package com.codecool.a38.kanban.issue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Label {

    private String labelId;

    private String title;

    private String color;

}
