package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.Login;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> authenticate(@RequestBody Login login) {
        return loginService.authenticate(login.getId(), login.getPassword(), login.getRole());
    }

}
