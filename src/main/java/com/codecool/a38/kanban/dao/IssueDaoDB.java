package com.codecool.a38.kanban.dao;

import com.codecool.a38.kanban.repository.IssueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IssueDaoDB implements IssueDao {

    private IssueRepository issueRepository;

}
