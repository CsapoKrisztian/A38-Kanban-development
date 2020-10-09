package com.codecool.a38.kanban.issue.model.graphQLResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PageInfo {

        @JsonProperty("hasNextPage")
        private boolean hasNextPage;

        @JsonProperty("endCursor")
        private String endCursor;

}
