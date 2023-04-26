package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.DelegateConsent;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.DoctorInterface;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;
import com.example.hadconsentservice.repository.DelegateConsentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService implements DoctorInterface {
    final ConsentItemRepository consentRepository;
    final DelegateConsentRepository delegateConsentRepository;
    final ConsentArtifactRepository consentArtifactRepository;
    public DoctorService(ConsentItemRepository consentRepository, DelegateConsentRepository delegateConsentRepository, ConsentArtifactRepository consentArtifactRepository) {
        this.consentRepository = consentRepository;
        this.delegateConsentRepository = delegateConsentRepository;
        this.consentArtifactRepository = consentArtifactRepository;
    }

    @Override
    public ResponseEntity<Response> sendConsentRequest(ConsentItem consentRequest) {
        try {
            consentRepository.save(consentRequest);
            return new ResponseEntity<>(new Response("Successfully created", 200), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Response> getConsentsByDoctorID(String doctorID) {
        List<ConsentItem> consentList = consentRepository.findAllByDoctorID(doctorID);
        List<ConsentItem> newConsentList = new ArrayList<>();
        for (ConsentItem item : consentList) {
            ConsentArtifact artifact = item.getConsentArtifact();
            // item.setIsDelegated(artifact.getIsDelegated());
            item.setEmergency(artifact.isEmergency());
            item.setArtifactID(String.valueOf(artifact.getArtifactId()));
            newConsentList.add(item);
        }
        return new ResponseEntity<>(new Response(newConsentList, 200), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> delegateConsent(DelegateConsent delegateConsent) {
        // TODO Auto-generated method stub
        delegateConsentRepository.save(delegateConsent);
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();


            FileWriter myWriter = new FileWriter("logs/consent_logs.txt", true);
            BufferedWriter br = new BufferedWriter(myWriter);
            br.write("\n"+"Delegate | TimeStamp;DelegateFromDoctorID;DelegateToDoctorID;DelegatePatientID"+"\n");
            br.write("\n"+dtf.format(now).toString() + ";" +delegateConsent.getFromDocID().toString()+";"+delegateConsent.getToDocID().toString()+";"+delegateConsent.getPatientID()+"\n");
            br.close();
            myWriter.close();

        } catch (Exception e){
            System.out.println("Logging Failed");
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(new Response(delegateConsent, 200), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getDelegateConsentsByFromDocID(String fromDocID) {
        // TODO Auto-generated method stub
        List<DelegateConsent> delegateConsents = delegateConsentRepository.findByFromDocID(fromDocID);
        for (DelegateConsent delegateConsent : delegateConsents) {
           delegateConsent.getConsentItemID().setArtifactID(String.valueOf(delegateConsent.getConsentItemID().getConsentArtifact().getArtifactId())); 
        }
        return new ResponseEntity<Response>(new Response(delegateConsents, 200), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getDelegateConsentsByToDocID(String toDocID) {
        // TODO Auto-generated method stub
        List<DelegateConsent> delegateConsents = delegateConsentRepository.findByToDocID(toDocID);
        for (DelegateConsent delegateConsent : delegateConsents) {
            delegateConsent.getConsentItemID().setArtifactID(String.valueOf(delegateConsent.getConsentItemID().getConsentArtifact().getArtifactId()));
         }

        return new ResponseEntity<Response>(new Response(delegateConsents, 200), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateDelegatedConsentStatus(Integer id, String docID) {
        // TODO Auto-generated method stub
        DelegateConsent delegateConsent = delegateConsentRepository.findById(id).get();
        if (delegateConsent != null) {
            delegateConsent.setIsDelegated(false);
            delegateConsentRepository.save(delegateConsent);
            try{
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
    
    
                FileWriter myWriter = new FileWriter("logs/consent_logs.txt", true);
                BufferedWriter br = new BufferedWriter(myWriter);
                br.write("\n"+"Revoke Delegated Consent | TimeStamp;DelegateFromDoctorID;DelegateToDoctorID;DelegatePatientID;isDelegated"+"\n");
                br.write("\n"+dtf.format(now).toString() + ";" +delegateConsent.getFromDocID().toString()+";"+delegateConsent.getToDocID().toString()+";"+delegateConsent.getPatientID()+";"+delegateConsent.getIsDelegated()+"\n");
                br.close();
                myWriter.close();
    
            } catch (Exception e){
                System.out.println("Logging Failed");
                System.out.println(e.getMessage());
            }
            return new ResponseEntity<Response>(new Response(delegateConsentRepository.findByFromDocID(docID), 200), HttpStatus.OK);
        }
        return new ResponseEntity<Response>(new Response("Delegation request not found", 404), HttpStatus.NOT_FOUND); 
    }

    @Override
    public ResponseEntity<Response> updateConsentStatus(ConsentRequest consentRequest) {
        // TODO Auto-generated method stub
        ConsentItem consentItem = consentRepository.findById(consentRequest.getItemId()).get();

        if (consentItem == null) {
            return new ResponseEntity<>(new Response("internal server error patientService", 400), HttpStatus.NOT_FOUND);
        }
        if (!consentItem.getDoctorID().equals(consentRequest.getDoctorId())) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        if (consentRequest.getConsentAcknowledged() != null)
            consentItem.setConsentAcknowledged(consentRequest.getConsentAcknowledged());
        if (consentRequest.getApproved() != null)
            consentItem.setApproved(consentRequest.getApproved());
        if (consentRequest.getOngoing() != null)
            consentItem.setOngoing(consentRequest.getOngoing());
        if (consentRequest.getIsDelegated() != null) 
            consentItem.setIsDelegated(consentRequest.getIsDelegated());
        consentRepository.save(consentItem);
        List<ConsentItem> consentItems = consentRepository.findAllByDoctorID(consentRequest.getDoctorId());
        return new ResponseEntity<>(new Response(consentItems, 200), HttpStatus.OK);
    }

}
