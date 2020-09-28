package com.codecool.a38.kanban.authorization.controller;

import com.codecool.a38.kanban.authorization.model.OAuthResponse;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private static final String APP_ID = "458f27c6eb357cf7419231331e3af3e3a9d39782b7edf50ac2cc083e7a7f1a4a";
    private static final String APP_SECRET = "f0fbf238c1ef5d0be56bf1118c430b15daff2b85d790d4bbfd76b8ccbb5bac33";
    private static final String REDIRECT_URI = "http://localhost:8080/getToken";

    private RestTemplate restTemplate;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/getToken")
    public void getToken(@RequestParam String code) {
        String url = "https://gitlab.techpm.guru/oauth/token";
        String parameters = "?client_id=" + APP_ID +
                "&client_secret=" + APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + REDIRECT_URI;

        HttpEntity<String> request = new HttpEntity<>(null);
        OAuthResponse oAuthResponse = restTemplate.postForEntity(url + parameters,
                request, OAuthResponse.class).getBody();

        if (oAuthResponse != null) {
            System.out.println("access token: " + oAuthResponse.getAccess_token());
        }
    }

}
