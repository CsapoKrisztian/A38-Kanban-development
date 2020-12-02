package com.codecool.a38.kanban.authorization.controller;

import com.codecool.a38.kanban.authorization.model.AppData;
import com.codecool.a38.kanban.authorization.model.OAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class AuthController {

    @Value("${gitlabServer.url}")
    private String gitlabServerUrl;

    @Value("${app.secret}")
    private String appSecret;

    private RestTemplate restTemplate;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * With the received code and application data, it sends a request to gitlab to get the token
     * as described in gitlab's Web application OAuth2 flow (https://docs.gitlab.com/ee/api/oauth2.html).
     * After that the token is put into an HTTP only cookie and the cookie is added to the response.
     * The cookie's max age is set either to the token's expiration time (if it exists) or one week.
     * @param response  the response in which the cookie will be set
     * @param code      the code received in query parameter
     * @param appData   an object that contains the app id and the redirect url
     * @return          returns ResponseEntity with a message about hte authentication process
     */
    @PostMapping("/getToken")
    public ResponseEntity<String> getToken(HttpServletResponse response, @RequestParam String code,
                                           @RequestBody AppData appData) {
        String gitlabServerOauthTokenUrl = gitlabServerUrl + "/oauth/token";
        String parameters = "?client_id=" + appData.getAppId() +
                "&client_secret=" + appSecret +
                "&code=" + code +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + appData.getRedirectUri();

        HttpEntity<String> request = new HttpEntity<>(null);
        OAuthResponse oAuthResponse = restTemplate.postForEntity(gitlabServerOauthTokenUrl + parameters,
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
