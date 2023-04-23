package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.Login;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.LoginInterface;
import com.example.hadconsentservice.repository.LoginnRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class LoginService implements LoginInterface {
    @Value("${bcrypt.hash}")
    private String hash;
    @Autowired
    private LoginnRepository loginRepository;

    @Override
    public ResponseEntity<Response> authenticate(Long id, String password, String role) {
        Login login = loginRepository.findByIdAndRole(id, role);
        String hashedPassword = BCrypt.hashpw(password,hash);
        if (login == null) {
            return new ResponseEntity<>(new Response("Incorrect Credentials", 400), HttpStatus.NOT_FOUND);
        }
        if (hashedPassword.equals(login.getPassword())) {
            login.setPassword(null);
            return new ResponseEntity<>(new Response(login, 200), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response("Incorrect Password", 400), HttpStatus.NOT_FOUND);
    }

}
