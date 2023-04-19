package com.example.hadconsentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hadconsentservice.bean.DelegateConsent;

public interface DelegateConsentRepository extends JpaRepository<DelegateConsent, Integer> {
    List<DelegateConsent> findByFromDocID(String fromDocID);
    List<DelegateConsent> findByToDocID(String toDocID);
    Optional<DelegateConsent> findById(Integer id);
}
