package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.AuthenticationResponse;
import com.example.hadconsentservice.bean.Login;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.LoginInterface;
import com.example.hadconsentservice.security.MyUserDetailsServiceImpl;
import com.example.hadconsentservice.security.TokenManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsServiceImpl myUserDetailsService;

    @Autowired
    LoginInterface loginInterface;

    @Autowired
    TokenManager tokenManager;

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody Login login) throws JsonProcessingException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    login.getId(), login.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(String.valueOf(login.getId()));
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        ResponseEntity<Response> responseEntity = loginInterface.authenticate(login.getId(), login.getPassword(), login.getRole());
        authenticationResponse.setAccessToken(tokenManager.generateToken(userDetails));

        authenticationResponse.setId(String.valueOf(login.getId()));
        authenticationResponse.setName(login.getName());
        authenticationResponse.setRole(login.getRole());
        ObjectMapper objectMapper = new ObjectMapper();
        String loginString = objectMapper.writeValueAsString(responseEntity.getBody().getObject());
        Login retreivedLogin = objectMapper.readValue(loginString, Login.class);
        authenticationResponse.setName(retreivedLogin.getName());
        return new ResponseEntity<>(new Response(authenticationResponse, 200), HttpStatus.OK);
    }

}

