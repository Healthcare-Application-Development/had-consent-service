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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ConsentArtifactService {

    @Autowired
    private ConsentArtifactRepository consentArtifactRepository;

    @Autowired
    private ConsentItemRepository consentItemRepository;

    public List<ConsentArtifact> findAllByPatientID(String patientID) {
        return consentArtifactRepository.findAllByPatientID(patientID);
    }


    public List<ConsentArtifact> revokeConsentArtifact(Integer artifactId) {
        ConsentArtifact consentArtifact = consentArtifactRepository.findById(artifactId)
                .orElseThrow(() -> new IllegalArgumentException("ConsentArtifact not found with artifactId: " + artifactId));
        List<ConsentItem> consentItems = consentArtifact.getConsentItems();
        for (ConsentItem consentItem : consentItems) {
            consentItem.setRevoked(true);
            consentItemRepository.save(consentItem);
        }

        consentArtifact.setRevoked(true);

        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();


            FileWriter myWriter = new FileWriter("logs/consent_logs.txt", true);
            BufferedWriter br = new BufferedWriter(myWriter);
            br.write("\n"+"Revoke artifact | TimeStamp;ArtifactID;PatientID;DoctorID;IsRevoked");
            br.write("\n"+dtf.format(now).toString() + ";" +consentArtifact.getArtifactId().toString()+";"+consentArtifact.getPatientID()+";"+consentArtifact.getDoctorID()+";"+consentArtifact.getRevoked()+"\n");
            br.close();
            myWriter.close();

        } catch (Exception e){
            System.out.println("Logging Failed");
            System.out.println(e.getMessage());
        }

        consentArtifactRepository.save(consentArtifact);

        return findAllByPatientID(consentArtifact.getPatientID());
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
        return new ResponseEntity<>(new Response(consentArtifactRepository.findAllByPatientID(patientID), 200), HttpStatus.OK);
    }

}

