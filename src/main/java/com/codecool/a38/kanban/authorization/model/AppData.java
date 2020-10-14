package com.codecool.a38.kanban.authorization.model;

import lombok.Data;

@Data
public class AppData {

    private String appId;

    private String appSecret;

    private String redirectUri;

}
