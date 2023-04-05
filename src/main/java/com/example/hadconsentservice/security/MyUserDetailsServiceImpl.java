package com.example.hadconsentservice.security;

import com.example.hadconsentservice.bean.Login;
import com.example.hadconsentservice.repository.LoginnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    LoginnRepository loginRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long id = Long.parseLong(username);
        Login login = loginRepository.findById(id).get();
        if (login == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        return new MyUserDetails(login);
    }
}
