package com.codecool.a38.kanban.config.service;

import com.codecool.a38.kanban.config.model.JsonProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class Initializer {

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<JsonProperties> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/configprops.json");
        try {
            JsonProperties jsonProperties = mapper.readValue(inputStream, typeReference);
            System.out.println(jsonProperties.toString());
        } catch (IOException e) {
            log.info("unable to read configprops.json");
            e.printStackTrace();
        }
    }

}
