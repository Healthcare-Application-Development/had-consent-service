package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.*;
import com.example.hadconsentservice.encryption.AESUtils;
import com.example.hadconsentservice.interfaces.LoginInterface;
import com.example.hadconsentservice.repository.GuardianRepository;
import com.example.hadconsentservice.security.MyUserDetailsServiceImpl;
import com.example.hadconsentservice.security.TokenManager;
import com.example.hadconsentservice.service.GuardianService;
import com.example.hadconsentservice.service.LoginService;
import com.example.hadconsentservice.service.PatientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.List;
import java.util.Optional;

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

    @Autowired
    AESUtils aesUtils;


    @Value("${CMS_SECRET_KEY}")
    String cmsSecretString;

    @Autowired
    private GuardianService guardianService;
    @Autowired
    private LoginService loginService;

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody Login login) throws Exception {
        String password = aesUtils.decrypt(login.getPassword(), cmsSecretString);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    login.getId(), password));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(String.valueOf(login.getId()));
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        Long id = login.getId();
        String role = login.getRole();

        System.out.println(role);
        if(role.equals("guardian")){
            System.out.println(id);
            Optional<Guardian> guardian = guardianService.findPatientById(id);
            id = guardian.get().getAssignedPatientID();
            authenticationResponse.setGuardianID(String.valueOf(id));
        }
        ResponseEntity<Response> responseEntity = loginInterface.authenticate(id, password, role);

        authenticationResponse.setAccessToken(tokenManager.generateToken(userDetails));

        authenticationResponse.setId(String.valueOf(login.getId()));
        authenticationResponse.setName(login.getName());
        authenticationResponse.setRole(login.getRole());
        //ObjectMapper objectMapper = new ObjectMapper();
        //String loginString = objectMapper.writeValueAsString(responseEntity.getBody().getObject());
        //Login retreivedLogin = objectMapper.readValue(loginString, Login.class);
        //authenticationResponse.setName(retreivedLogin.getName());
        return new ResponseEntity<>(new Response(authenticationResponse, 200), HttpStatus.OK);
    }

}

