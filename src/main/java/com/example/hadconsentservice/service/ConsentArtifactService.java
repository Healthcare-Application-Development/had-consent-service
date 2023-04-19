package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    public ResponseEntity<Response> updateConsentStatus(ConsentRequest consentRequest, String patientID) {
        ConsentArtifact consentArtifact = consentArtifactRepository.findById(consentRequest.getItemId()).get();

        if (consentArtifact == null) {
            return new ResponseEntity<>(new Response("internal server error patientService", 400), HttpStatus.NOT_FOUND);
        }
        if (!consentArtifact.getPatientID().equals(Integer.valueOf(patientID))) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        consentArtifact.setConsentAcknowledged(consentRequest.getConsentAcknowledged());
        consentArtifact.setApproved(consentRequest.getApproved());
        consentArtifact.setOngoing(consentRequest.getOngoing());
        consentArtifactRepository.save(consentArtifact);
        return new ResponseEntity<>(new Response(consentArtifact, 200), HttpStatus.OK);
    }

}

