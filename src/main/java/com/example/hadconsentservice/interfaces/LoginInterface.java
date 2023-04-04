package com.example.hadconsentservice.interfaces;

public interface LoginInterface {
    boolean authenticate(Long id, String password, String role);
}
