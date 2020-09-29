package com.codecool.a38.kanban.authorization.model;

import lombok.Data;

@Data
public class OAuthResponse {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private String scope;

    private String created_at;

}
