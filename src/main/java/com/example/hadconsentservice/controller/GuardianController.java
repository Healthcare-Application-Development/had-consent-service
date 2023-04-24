package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.Login;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.bean.Guardian;
import com.example.hadconsentservice.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/guardian")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class GuardianController {

    private LoginService loginService;

    public GuardianController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> authenticate(@RequestBody Login login) {
        return loginService.authenticate(login.getId(), login.getPassword(), login.getRole());
    }

}
