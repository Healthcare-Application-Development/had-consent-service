package com.example.hadconsentservice.controller;


import com.example.hadconsentservice.bean.*;
import com.example.hadconsentservice.encryption.AESUtils;
import com.example.hadconsentservice.interfaces.MediatorServiceRequestInterface;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;
import com.example.hadconsentservice.repository.DelegateConsentRepository;
import com.example.hadconsentservice.repository.MediatorServiceRequestRepository;
import com.example.hadconsentservice.security.TokenManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/mediatorServiceRequestController")
public class MediatorServiceRequestController {
    @Value("${mediator_username}")
    String mediatorUsername;

    @Value("${mediator_pass}")
    String mediatorPassword;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    AESUtils aesUtils;

    @Autowired
    ConsentItemRepository consentItemRepository;

    @Autowired
    DelegateConsentRepository delegateConsentRepository;

    final MediatorServiceRequestInterface mediatorServiceRequestInterface;
    final ConsentArtifactRepository consentArtifactRepository;

    public MediatorServiceRequestController(MediatorServiceRequestInterface mediatorServiceRequestInterface, ConsentArtifactRepository consentArtifactRepository) {
        this.mediatorServiceRequestInterface = mediatorServiceRequestInterface;
        this.consentArtifactRepository = consentArtifactRepository;
    }


    @PostMapping("/call-mediator-service")
    public ResponseEntity<List<PatientHealthRecord>> getPatientDataByIdAndRecordType(@RequestHeader("Authorization") String authorization, @RequestBody ConsentArtifact consentArtifact) throws Exception {
        String extractedToken = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(extractedToken);
        username = aesUtils.encrypt(username);
        boolean isDelegation = false;
        DelegateConsent delegateConsent = null;
        if (consentArtifact.getDelegationID() != null) {
            isDelegation = true;
            delegateConsent = delegateConsentRepository.findById(Integer.parseInt(consentArtifact.getDelegationID())).get();
        }
        if ((!isDelegation && !username.equals(consentArtifact.getDoctorID())) || (isDelegation && !delegateConsent.getToDocID().equals(username))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        //Storing the artifact
        if (consentArtifact.isEmergency()) {
            ConsentArtifact savedArtifact = consentArtifactRepository.save(consentArtifact);
            for (ConsentItem consentItem : consentArtifact.getConsentItems()) {
                consentItem.setConsentArtifact(consentArtifact);
                consentItemRepository.save(consentItem);
            }
            consentArtifact = savedArtifact;
        }
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("username", mediatorUsername);
        params.put("password", mediatorPassword);
        params.put("role", "ADMIN");
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> tokenEntity = new HttpEntity<>(params, tokenHeaders);
        ResponseEntity<Response> tokenResponseEntity = restTemplate.exchange("http://localhost:9108/authenticate", HttpMethod.POST, tokenEntity,  new ParameterizedTypeReference<Response>() {});
        Response response = tokenResponseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        String tokenAsString = objectMapper.writeValueAsString(response.getObject());
        AuthenticationResponse authenticationResponse = objectMapper.readValue(tokenAsString, AuthenticationResponse.class);
        String token = authenticationResponse.getAccessToken();
        ConsentArtifact artifact = consentArtifactRepository.findById(consentArtifact.getArtifactId()).get();
        String encryptedDoctorID = artifact.getDoctorID();
        if ((!isDelegation && !username.equals(encryptedDoctorID)) || (isDelegation && !delegateConsent.getToDocID().equals(username))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        //Checking consentArtifact if its acknowledged and approved
        if(consentArtifact.isEmergency() || (artifact.getConsentAcknowledged() && artifact.getApproved())){
            List<ConsentItem> consentItemList=artifact.getConsentItems();
            List<ConsentItem> consentItemListFiltered=new ArrayList<>();
            for (int i = 0; i < consentItemList.size(); i++) {
                ConsentItem consentItemTemp=consentItemList.get(i);
                if(consentArtifact.isEmergency() || (consentItemTemp.getConsentAcknowledged() && consentItemTemp.getApproved())) {
                    consentItemListFiltered.add(consentItemTemp);
                }
            }
            artifact.setConsentItems(consentItemListFiltered);

            //calling mediator service
            String endpointUrl = "http://localhost:9108/patientHealthRecord/getPatientHealthRecord";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + token);
            HttpEntity<ConsentArtifact> requestEntity = new HttpEntity<>(artifact, headers);
            ResponseEntity<List<PatientHealthRecord>> responseEntity =restTemplate.exchange(endpointUrl,HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<PatientHealthRecord>>() {});
            List<PatientHealthRecord> records = responseEntity.getBody();

            return new ResponseEntity<List<PatientHealthRecord>>(records, HttpStatus.OK);

        }
        else{
            return new ResponseEntity<List<PatientHealthRecord>>(HttpStatus.EXPECTATION_FAILED);

        }

    }


}
