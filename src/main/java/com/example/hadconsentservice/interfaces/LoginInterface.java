package com.example.hadconsentservice.interfaces;

import com.example.hadconsentservice.bean.Response;
import org.springframework.http.ResponseEntity;

public interface LoginInterface {
    ResponseEntity<Response> authenticate(Long id, String password, String role);
}
