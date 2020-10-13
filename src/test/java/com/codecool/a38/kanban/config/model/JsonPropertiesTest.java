package com.codecool.a38.kanban.config.model;

import com.codecool.a38.kanban.KanbanApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = KanbanApplication.class)
class JsonPropertiesTest {

    @Autowired
    private JsonProperties jsonProperties;

    @Test
    public void whenPropertiesLoadedViaJsonPropertySource_thenLoadFlatValues() {
        assertEquals("story:", jsonProperties.getStoryPrefix());
    }


}