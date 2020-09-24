package com.codecool.a38.kanban.issue.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("production")
@AllArgsConstructor
public class DataInitializer {

    private DataManager dataManager;

    @PostConstruct
    public void init() {
        dataManager.refreshData();
    }

}
