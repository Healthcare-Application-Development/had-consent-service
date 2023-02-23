package com.example.hadconsentservice.repository;

import com.example.hadconsentservice.bean.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsentRepository extends JpaRepository<Consent, Integer> {
    List<Consent> findAllByPatientID(Integer patientID);
    List<Consent> findAllByDoctorID(Integer doctorID);
}
