package com.example.hadconsentservice.repository;

import com.example.hadconsentservice.bean.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginnRepository extends JpaRepository<Login, Long> {
    Login findByIdAndRole(Long id, String role);
    Optional<Login> findById(Long id);

}