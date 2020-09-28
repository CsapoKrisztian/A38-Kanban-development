package com.codecool.a38.kanban.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CodeController {

    @GetMapping("/code")
    public void codeHandler(@RequestParam String code) {
        System.out.println(code);
    }

}
