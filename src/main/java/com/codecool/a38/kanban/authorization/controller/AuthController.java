package com.codecool.a38.kanban.authorization.controller;

import com.codecool.a38.kanban.authorization.model.AppData;
import com.codecool.a38.kanban.authorization.model.OAuthResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {

    private RestTemplate restTemplate;

    @PostMapping("/getToken")
    public ResponseEntity<String> getToken(HttpServletResponse response, @RequestParam String code,
                                           @RequestBody AppData appData) {
        String url = "https://gitlab.techpm.guru/oauth/token";
        String parameters = "?client_id=" + appData.getAppId() +
                "&client_secret=" + appData.getAppSecret() +
                "&code=" + code +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + appData.getRedirectUri();

        HttpEntity<String> request = new HttpEntity<>(null);
        OAuthResponse oAuthResponse = restTemplate.postForEntity(url + parameters,
                request, OAuthResponse.class).getBody();
        if (oAuthResponse != null) {
            String gitlabAccessToken = oAuthResponse.getAccess_token();
            log.info("gitlab access token received from gitlab: " + gitlabAccessToken);

            Cookie cookie = new Cookie("gitlabAccessToken", gitlabAccessToken);

            int weekInSeconds = 60 * 60 * 24 * 7;
            int maxAge = oAuthResponse.getExpires_in() != null ? oAuthResponse.getExpires_in() : weekInSeconds;
            cookie.setMaxAge(maxAge);

            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok("accessToken saved in cookie: " + gitlabAccessToken);

        } else {
            log.info("No oauth response returned");
            return ResponseEntity.ok("OAuthResponse not returned");
        }
    }

}
