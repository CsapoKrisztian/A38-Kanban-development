package com.codecool.a38.kanban.dao;

import com.codecool.a38.kanban.model.Issue;
import com.codecool.a38.kanban.repository.IssueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IssueDaoDB implements IssueDao {

    private IssueRepository issueRepository;

    @Override
    public List<Issue> getAll() {
        return issueRepository.findAll();
    }

    @Override
    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }

}
