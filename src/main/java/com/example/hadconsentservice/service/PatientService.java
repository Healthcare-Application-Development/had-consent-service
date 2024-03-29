package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.*;
import com.example.hadconsentservice.interfaces.PatientInterface;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PatientService implements PatientInterface {
    final ConsentItemRepository consentRepository;
    @Autowired
    ConsentArtifactRepository consentArtifactRepository;

    @Autowired
    GuardianService guardianService;


    public PatientService(ConsentItemRepository consentRepository) {
        this.consentRepository = consentRepository;
    }

    @Override
    public ResponseEntity<Response> getAllConsentsByID(String patientID) {
        List<ConsentItem> consentList = consentRepository.findAllByPatientID(patientID);
        List<ConsentItem> updatedConsentList = new ArrayList<>();

        for (ConsentItem consent: consentList) {
            if (!consent.getConsentAcknowledged() || (consent.getConsentAcknowledged() && consent.getApproved())) {
                updatedConsentList.add(consent);
            }
        }
        return new ResponseEntity<>(new Response(updatedConsentList, 200), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateConsentStatus(ConsentRequest consentRequest, String patientID) {
        List<ConsentItem> consentItem = consentRepository.findAllById(consentRequest.getItemId());
        if (!consentRequest.getPatientId().equals((patientID))) {
            return new ResponseEntity<>(new Response("Authorization failed", 403), HttpStatus.FORBIDDEN);
        }
        for (ConsentItem consent:consentItem) {
            if (!consent.getPatientID().equals((patientID))) {
                return new ResponseEntity<>(new Response("Authorization failed", 403), HttpStatus.FORBIDDEN);
            }
            if (consent.getConsentAcknowledged()) {
                return new ResponseEntity<>(new Response("Consent Already Acknowledged", 404), HttpStatus.NOT_FOUND);
            }
            consent.setConsentAcknowledged(true);

            consent.setApproved(consentRequest.getApproved());
            consent.setOngoing(consentRequest.getOngoing());
            consent.setIsDelegated(consentRequest.getIsDelegated());

            try{
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();


                FileWriter myWriter = new FileWriter("logs/consent_logs.txt", true);
                BufferedWriter br = new BufferedWriter(myWriter);
                br.write("\n"+"Update Consent | TimeStamp;PatientID;DoctorID;IsApproved;ArtifactID;IsDelegated;IsOngoing;FromDate;ToDate;Approved");
                br.write("\n"+dtf.format(now).toString() + ";" +consent.getPatientID().toString()+";"+consent.getDoctorID().toString()+";"+consent.getApproved().toString()+";"+consent.getArtifactID()+";"+consent.getDelegationRequired().toString()+";"+consent.getOngoing().toString()+";"+consent.getFromDate().toString()+";"+consent.getToDate().toString()+";"+consent.getApproved());
                br.close();
                myWriter.close();

            } catch (Exception e){
                System.out.println("Logging Failed");
                System.out.println(e.getMessage());
            }

            try {
                consentRepository.save(consent);
                return new ResponseEntity<>(new Response(consentArtifactRepository.findAllByPatientID(patientID), 200), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new Response(e.getMessage(), 400), HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(new Response("Unable to find patient records", 400), HttpStatus.NOT_FOUND);

    }

    public String get_patientID_from_guardianID(String guardianID){
        String patient_id;
        Optional<Guardian> guardian = guardianService.findPatientById(Long.parseLong(guardianID));
        try{
            patient_id = String.valueOf(guardian.get().getAssignedPatientID());
            return patient_id;
        }
        catch(Exception e){
            return "";
        }


    }

}
