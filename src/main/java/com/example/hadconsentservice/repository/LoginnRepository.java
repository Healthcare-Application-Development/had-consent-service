package com.example.hadconsentservice.repository;

import com.example.hadconsentservice.bean.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginnRepository extends JpaRepository<Login, Long> {
    Login findByIdAndRole(Long id, String role);

}