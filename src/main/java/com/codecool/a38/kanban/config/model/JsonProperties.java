package com.codecool.a38.kanban.config.model;

import com.codecool.a38.kanban.config.service.JsonPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource(
        value = "classpath:configprops.json",
        factory = JsonPropertySourceFactory.class)
@ConfigurationProperties
@Data
public class JsonProperties {

    private String storyPrefix;

    private List<PriorityProperty> priorities;

    private List<StatusProperty> statuses;

}
