package com.codecool.a38.kanban.config.service;

import com.codecool.a38.kanban.config.model.JsonProperties;
import com.codecool.a38.kanban.config.model.LabelProperty;
import com.codecool.a38.kanban.config.model.PriorityDisplayNum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Data
@Slf4j
public class ConfigDataProvider {

    private String storyPrefix;

    private List<String> statusTitles;

    private Map<String, String> statusTitleDisplayMap = new HashMap<>();

    private Map<String, String> statusDisplayTitleMap = new HashMap<>();

    private Map<String, PriorityDisplayNum> priorityTitleDisplayNumMap = new HashMap<>();

    private final String configJsonFilePath = "/configprops.json";

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<JsonProperties> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream(configJsonFilePath);
        try {
            JsonProperties jsonProperties = mapper.readValue(inputStream, typeReference);
            setStoryPrefix(jsonProperties);
            setStatusTitles(jsonProperties);
            setStatusTitleDisplayMap(jsonProperties);
            setStatusDisplayTitleMap(jsonProperties);
            setPriorityTitleDisplayNumMap(jsonProperties);
        } catch (IOException e) {
            log.info("Unable to read file: " + configJsonFilePath);
            e.printStackTrace();
        }
    }

    private void setStoryPrefix(JsonProperties jsonProperties) {
        storyPrefix = jsonProperties.getStoryPrefix();
        log.info("Story prefix loaded from config Json: " + storyPrefix);
    }

    private void setStatusTitles(JsonProperties jsonProperties) {
        statusTitles = jsonProperties.getStatuses().stream()
                .map(LabelProperty::getDisplay)
                .collect(Collectors.toList());
        log.info("Status titles loaded from config Json: " + statusTitles.toString());
    }

    private void setStatusTitleDisplayMap(JsonProperties jsonProperties) {
        statusTitleDisplayMap = jsonProperties.getStatuses().stream()
                .collect(Collectors.toMap(LabelProperty::getTitle, LabelProperty::getDisplay));
        log.info("Status title display map loaded from config Json: " + statusTitleDisplayMap.toString());
    }

    private void setStatusDisplayTitleMap(JsonProperties jsonProperties) {
        statusDisplayTitleMap = jsonProperties.getStatuses().stream()
                .collect(Collectors.toMap(LabelProperty::getDisplay, LabelProperty::getTitle));
    }

    private void setPriorityTitleDisplayNumMap(JsonProperties jsonProperties) {
        int serial = 0;
        for (LabelProperty labelProperty : jsonProperties.getPriorities()) {
            priorityTitleDisplayNumMap.put(labelProperty.getTitle(),
                    new PriorityDisplayNum(labelProperty.getDisplay(), serial++));
        }
        log.info("Priority title display map loaded from config Json: " + priorityTitleDisplayNumMap.toString());
    }

}
