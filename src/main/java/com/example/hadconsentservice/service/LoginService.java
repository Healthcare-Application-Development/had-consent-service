package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.Login;
import com.example.hadconsentservice.interfaces.LoginInterface;
import com.example.hadconsentservice.repository.LoginnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginService implements LoginInterface {
    @Autowired
    private LoginnRepository loginRepository;

    @Override
    public boolean authenticate(Long id, String password, String role) {
        Login login = loginRepository.findByIdAndRole(id, role);
        if (login == null) {
            return false;
        }
        return password.equals(login.getPassword());
    }

}
