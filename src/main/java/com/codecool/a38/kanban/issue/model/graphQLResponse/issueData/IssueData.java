package com.codecool.a38.kanban.issue.model.graphQLResponse.issueData;

import com.codecool.a38.kanban.issue.model.graphQLResponse.IssueNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssueData {

	@JsonProperty("issue")
	private IssueNode issue;

}
