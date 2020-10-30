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

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<JsonProperties> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/configprops.json");
        try {
            JsonProperties jsonProperties = mapper.readValue(inputStream, typeReference);
            setDataFromJsonProperties(jsonProperties);
            log.info("Config properties loaded from configprops.json");
            log.info("Status titles: " + statusTitles.toString());
            log.info("Story prefix: " + storyPrefix);
            log.info("statusTitleDisplayMap: " + statusTitleDisplayMap.toString());
            log.info("priorityTitleDisplayMap: " + priorityTitleDisplayNumMap.toString());

        } catch (IOException e) {
            log.info("Unable to read configprops.json");
            e.printStackTrace();
        }
    }

    private void setDataFromJsonProperties(JsonProperties jsonProperties) {
        storyPrefix = jsonProperties.getStoryPrefix();

        statusTitles = jsonProperties.getStatuses().stream()
                .map(LabelProperty::getDisplay)
                .collect(Collectors.toList());

        statusTitleDisplayMap = jsonProperties.getStatuses().stream()
                .collect(Collectors.toMap(LabelProperty::getTitle, LabelProperty::getDisplay));

        statusDisplayTitleMap = jsonProperties.getStatuses().stream()
                .collect(Collectors.toMap(LabelProperty::getDisplay, LabelProperty::getTitle));

        int serial = 0;
        for (LabelProperty labelProperty : jsonProperties.getPriorities()) {
            priorityTitleDisplayNumMap.put(labelProperty.getTitle(),
                    new PriorityDisplayNum(labelProperty.getDisplay(), serial++));
        }

    }

}
