package com.codecool.a38.kanban.controller;

import com.codecool.a38.kanban.dao.IssueDao;
import com.codecool.a38.kanban.model.Issue;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class IssueController {

    private IssueDao issueDao;

    @GetMapping("/issues")
    public List<Issue> getIssues() {
        return issueDao.getAll();
    }

}
