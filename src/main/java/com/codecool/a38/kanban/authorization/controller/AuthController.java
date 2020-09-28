package com.codecool.a38.kanban.authorization.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private static final String applicationId = "458f27c6eb357cf7419231331e3af3e3a9d39782b7edf50ac2cc083e7a7f1a4a";
    private static final String secret = "f0fbf238c1ef5d0be56bf1118c430b15daff2b85d790d4bbfd76b8ccbb5bac33";
    private static final String redir = "http://localhost:8080/getToken";


    @GetMapping("/getToken")
    public void getToken(@RequestParam String code) {
        System.out.println("get Token: " + code);

        String grant_type = "authorization_code";



    }

}
