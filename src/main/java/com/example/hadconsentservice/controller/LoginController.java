package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public boolean authenticate(@RequestParam Long id, @RequestParam String password, @RequestParam String role) {
        return loginService.authenticate(id, password, role);
    }

}
