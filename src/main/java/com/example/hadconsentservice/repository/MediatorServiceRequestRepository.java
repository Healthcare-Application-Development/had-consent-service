package com.example.hadconsentservice.repository;

import com.example.hadconsentservice.bean.ConsentArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediatorServiceRequestRepository extends JpaRepository<ConsentArtifact, Integer> {

}
