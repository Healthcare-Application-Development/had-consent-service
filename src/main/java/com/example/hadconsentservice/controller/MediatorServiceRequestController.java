package com.example.hadconsentservice.controller;


import com.example.hadconsentservice.bean.*;
import com.example.hadconsentservice.interfaces.MediatorServiceRequestInterface;
import com.example.hadconsentservice.repository.MediatorServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mediatorServiceRequestController")
public class MediatorServiceRequestController {

    final MediatorServiceRequestInterface mediatorServiceRequestInterface;
    final MediatorServiceRequestRepository mediatorServiceRequestRepository;

    public MediatorServiceRequestController(MediatorServiceRequestInterface mediatorServiceRequestInterface, MediatorServiceRequestRepository mediatorServiceRequestRepository) {
        this.mediatorServiceRequestInterface = mediatorServiceRequestInterface;
        this.mediatorServiceRequestRepository = mediatorServiceRequestRepository;
    }


    @PostMapping("/call-mediator-service")
    public ResponseEntity<List<PatientHealthRecord>> getPatientDataByIdAndRecordType(@RequestBody ConsentArtifact consentArtifact) {

        //Storing the artifact
        ConsentArtifact consentArtifactItem = mediatorServiceRequestRepository.save(consentArtifact);

        //Checking consentArtifact if its acknowledged and approved
        if(consentArtifact.isEmergency() || (consentArtifact.getConsentAcknowledged() && consentArtifact.getApproved())){
            ConsentArtifact consentArtifactRequest=new ConsentArtifact();
            consentArtifactRequest.setPatientID(consentArtifact.getPatientID());
            consentArtifactRequest.setArtifactId(consentArtifact.getArtifactId());
            consentArtifactRequest.setApproved(consentArtifact.getApproved());
            consentArtifactRequest.setEmergency(consentArtifact.isEmergency());
            consentArtifactRequest.setConsentAcknowledged(consentArtifact.getConsentAcknowledged());
            consentArtifactRequest.setDoctorID(consentArtifact.getPatientID());
            consentArtifactRequest.setTimestamp(consentArtifact.getTimestamp());
            consentArtifactRequest.setOngoing(consentArtifact.getOngoing());
            List<ConsentItem> consentItemList=consentArtifact.getConsentItems();
            List<ConsentItem> consentItemListFiltered=new ArrayList<>();
            for (int i = 0; i < consentItemList.size(); i++) {
                ConsentItem consentItemTemp=consentItemList.get(i);
                if(consentArtifact.isEmergency() || (consentItemTemp.getConsentAcknowledged() && consentItemTemp.getApproved())) {
                    consentItemListFiltered.add(consentItemTemp);
                }
            }
            consentArtifactRequest.setConsentItems(consentItemListFiltered);

            //calling mediator service
            RestTemplate restTemplate = new RestTemplate();
            String endpointUrl = "http://localhost:8080/patientHealthRecord/getPatientHealthRecord";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ConsentArtifact> requestEntity = new HttpEntity<>(consentArtifactRequest, headers);
            ResponseEntity<List<PatientHealthRecord>> responseEntity =restTemplate.exchange(endpointUrl,HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<PatientHealthRecord>>() {});
            List<PatientHealthRecord> records = responseEntity.getBody();

            return new ResponseEntity<List<PatientHealthRecord>>(records, HttpStatus.OK);

        }
        else{
            return new ResponseEntity<List<PatientHealthRecord>>(HttpStatus.EXPECTATION_FAILED);

        }

    }


}
