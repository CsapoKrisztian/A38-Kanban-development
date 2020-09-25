package com.codecool.a38.kanban.controller;

import com.codecool.a38.kanban.services.APIService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private APIService apiService;

    public AuthController(APIService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/signin")
    public ResponseEntity signIn() {
        try {
            apiService.getCode();
            List<String> stg = apiService.getCode().getHeaders().get(0);
            //apiService.getTheSignIn()

            return ResponseEntity.ok("");
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username / password");
        }
    }

}
