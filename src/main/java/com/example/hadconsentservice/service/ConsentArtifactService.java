package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsentArtifactService {

    @Autowired
    private ConsentArtifactRepository consentArtifactRepository;

    public List<ConsentArtifact> findAllByPatientID(Integer patientID) {
        return consentArtifactRepository.findAllByPatientID(patientID);
    }


    public ConsentArtifact revokeConsentArtifact(Integer artifactId) {
        ConsentArtifact consentArtifact = consentArtifactRepository.findById(artifactId)
                .orElseThrow(() -> new IllegalArgumentException("ConsentArtifact not found with artifactId: " + artifactId));

        consentArtifact.setRevoked(true);
        consentArtifactRepository.save(consentArtifact);

        return consentArtifact;
    }


}

