package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsentArtifactService {

    @Autowired
    private ConsentArtifactRepository consentArtifactRepository;

    public List<ConsentArtifact> findAllByPatientID(String patientID) {
        return consentArtifactRepository.findAllByPatientID(patientID);
    }

    public ResponseEntity<Response> updateConsentStatus(ConsentRequest consentRequest, String patientID) {
        ConsentArtifact consentArtifact = consentArtifactRepository.findById(consentRequest.getItemId()).get();

        if (consentArtifact == null) {
            return new ResponseEntity<>(new Response("internal server error patientService", 400), HttpStatus.NOT_FOUND);
        }
        if (!consentArtifact.getPatientID().equals(patientID)) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        if (consentRequest.getConsentAcknowledged() != null)
            consentArtifact.setConsentAcknowledged(consentRequest.getConsentAcknowledged());
        if (consentRequest.getApproved() != null)
            consentArtifact.setApproved(consentRequest.getApproved());
        if (consentRequest.getOngoing() != null)
            consentArtifact.setOngoing(consentRequest.getOngoing());
        // if (consentRequest.getIsDelegated() != null) 
        //     consentArtifact.setIsDelegated(consentRequest.getIsDelegated());
        consentArtifactRepository.save(consentArtifact);
        return new ResponseEntity<>(new Response(consentArtifact, 200), HttpStatus.OK);
    }

}

