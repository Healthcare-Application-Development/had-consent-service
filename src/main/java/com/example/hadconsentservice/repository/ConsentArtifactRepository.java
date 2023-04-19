package com.example.hadconsentservice.repository;

import com.example.hadconsentservice.bean.ConsentArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsentArtifactRepository extends JpaRepository<ConsentArtifact, Integer> {




    List<ConsentArtifact> findAllByPatientID(Integer patientID);
}