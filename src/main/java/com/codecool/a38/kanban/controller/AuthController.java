package com.codecool.a38.kanban.controller;

import com.codecool.a38.kanban.services.APIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private APIService apiService;

    public AuthController(APIService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/signin")
    public ResponseEntity signIn() {
        return ResponseEntity.ok("");
    }

}
