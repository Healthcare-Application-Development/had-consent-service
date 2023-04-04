package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.Login;
import com.example.hadconsentservice.interfaces.LoginInterface;
import com.example.hadconsentservice.repository.LoginnRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class LoginService implements LoginInterface {
    @Value("${bcrypt.hash}")
    private String hash;
    @Autowired
    private LoginnRepository loginRepository;

    @Override
    public boolean authenticate(Long id, String password, String role) {
        Login login = loginRepository.findByIdAndRole(id, role);
        String hashedPassword = BCrypt.hashpw(password,hash);
        System.out.println(hashedPassword);

        if (login == null) {
            return false;
        }
        return hashedPassword.equals(login.getPassword());
    }

}
