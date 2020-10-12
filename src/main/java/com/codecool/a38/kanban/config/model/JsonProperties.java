package com.codecool.a38.kanban.config.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class JsonProperties {

    private String storyPrefix;

    private List<LabelProperty> priorityProperties;

    private List<LabelProperty> statusProperties;

}
