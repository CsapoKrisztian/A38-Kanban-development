package com.codecool.a38.kanban.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIService {

    private final String appId = "bf4d532fe1efb0ca152242664404a275e9440424dda0144c7cb5eeb347c0004e";
    private final String redirect = "http://localhost:8080/";
    private final String secret = "4fcc92108aef85c51475e3c687509cd946885ea2e4084efe95fdbd5c27b99a93";

    public ResponseEntity<String> getCode() {
        String apiPath = "https://gitlab.com/oauth/authorize?client_id=" + appId + "&redirect_uri=" + redirect + "&response_type=code";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiPath, HttpMethod.GET, null, String.class);
        return responseEntity;
    }


}
