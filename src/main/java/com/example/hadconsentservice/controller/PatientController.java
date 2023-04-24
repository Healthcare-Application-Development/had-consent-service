package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.AuthenticationResponse;
import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.ConsentRevoke;
import com.example.hadconsentservice.bean.PatientHealthRecord;
import com.example.hadconsentservice.bean.PatientHealthRecordRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.encryption.AESUtils;
import com.example.hadconsentservice.interfaces.PatientInterface;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;
import com.example.hadconsentservice.security.TokenManager;
import com.example.hadconsentservice.service.ConsentArtifactService;
import com.example.hadconsentservice.service.ConsentService;
import com.example.hadconsentservice.service.PatientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/patient")
public class PatientController {
    final PatientInterface patientInterface;

    public PatientController(PatientInterface patientInterface) {
        this.patientInterface = patientInterface;
    }

    @Autowired
    TokenManager tokenManager;
    
    @Autowired
    private ConsentArtifactService consentArtifactService;
    
    @Autowired
    private ConsentService consentService;
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private ConsentArtifactRepository consentArtifactRepository;
    
    @Autowired
    private ConsentItemRepository consentItemRepository;
    
    @Autowired
    AESUtils aesUtils;
    
    @Value("${mediator_username}")
    String mediatorUsername;

    @Value("${mediator_pass}")
    String mediatorPassword;


    @Value("${CMS_SECRET_KEY}")
    String cmsSecretString;

    @Value("${HOSPITAL_SECRET_KEY}")
    String hospitalSecretKey;
    String temp_patient_id;

    @GetMapping("/getAllConsents")
    public ResponseEntity<Response> findAllByPatientID(@RequestHeader("Authorization") String authorization, @PathParam("id") String id) throws Exception {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        temp_patient_id = patientService.get_patientID_from_guardianID(username);
        username = aesUtils.encrypt(username, cmsSecretString);

        //System.out.println(temp_patient_id.equals("") && !username.equals(id));

        if ((temp_patient_id.equals("") && !username.equals(id)) || (!temp_patient_id.equals("") && !aesUtils.encrypt(temp_patient_id, cmsSecretString).equals(id))) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        List<ConsentArtifact> ca = consentArtifactService.findAllByPatientID(id);
        return new ResponseEntity<>(new Response(ca, 200), HttpStatus.OK);
    }

    @PutMapping("/updateConsentItemStatus")
    public ResponseEntity<Response> updateConsentItemStatus(@RequestHeader("Authorization") String authorization, @RequestBody ConsentRequest consentRequest) throws Exception {
        String patientID = consentRequest.getPatientId();
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        temp_patient_id = patientService.get_patientID_from_guardianID(username);
        username = aesUtils.encrypt(username, cmsSecretString);
        System.out.println("HERE");
        if ((temp_patient_id.equals("") && !username.equals(patientID)) || (!temp_patient_id.equals("") && !aesUtils.encrypt(temp_patient_id, cmsSecretString).equals(patientID))) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        ResponseEntity<Response> res = patientInterface.updateConsentStatus(consentRequest, String.valueOf(patientID));
        if (res.getStatusCode() == HttpStatus.FORBIDDEN) {
            return res;
        }
        if (res.getStatusCode() == HttpStatus.NOT_FOUND) {
            return res;
        }
        return res;
    }

    @PutMapping("/updateConsentStatus")
    public ResponseEntity<Response> updateConsentStatus(@RequestHeader("Authorization") String authorization, @RequestBody ConsentRequest consentRequest) throws Exception {
        String patientID = consentRequest.getPatientId();
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        temp_patient_id = patientService.get_patientID_from_guardianID(username);
        username = aesUtils.encrypt(username, cmsSecretString);
        if ((temp_patient_id.equals("") && !username.equals(String.valueOf(patientID))) || (!temp_patient_id.equals("") && !aesUtils.encrypt(temp_patient_id, cmsSecretString).equals(patientID))) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        ResponseEntity<Response> res = consentArtifactService.updateConsentStatus(consentRequest, String.valueOf(patientID));
        if (res.getStatusCode() == HttpStatus.FORBIDDEN) {
            return res;
        }
        return res;
    }
    @PostMapping("/consent-artifacts/revoke")
    public ResponseEntity<Response> revokeConsentArtifact(@RequestHeader("Authorization") String authorization, @RequestBody ConsentRevoke consentRevoke) throws Exception {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        username = aesUtils.encrypt(username, cmsSecretString);
        ConsentArtifact artifact = consentArtifactRepository.findById(consentRevoke.getId()).get();
        if (!username.equals(artifact.getPatientID())) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new Response(consentArtifactService.revokeConsentArtifact(consentRevoke.getId()), 200), HttpStatus.OK);
    }

    @PostMapping("/consent-item/revoke")
    public ResponseEntity<Response> revokeConsentArtifactitem(@RequestHeader("Authorization") String authorization, @RequestBody ConsentRevoke consentRevoke) throws Exception {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        username = aesUtils.encrypt(username, cmsSecretString);
        ConsentItem consentItem = consentItemRepository.findById(consentRevoke.getId()).get();
        if (!username.equals(consentItem.getPatientID())) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Response>(new Response(consentService.revokeConsentArtifactItem(consentRevoke.getId()), 200), HttpStatus.OK);
    }

    @PostMapping("/getHealthRecords")
    public ResponseEntity<Response> getPatientHealthRecordsByABHAID(@RequestHeader("Authorization") String authorization, @RequestBody PatientHealthRecordRequest patientHealthRecordRequest) throws Exception {
        String extractedToken = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(extractedToken);
        username = aesUtils.encrypt(username, cmsSecretString);
        if (!username.equals(patientHealthRecordRequest.getAbhaID())) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
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
        String connectionURL = "http://localhost:9108/patientHealthRecord/getPatientHealthRecordByAbhaId?id=" + URLEncoder.encode(patientHealthRecordRequest.getAbhaID(), StandardCharsets.UTF_8);
        HttpHeaders healthRecordHeaders = new HttpHeaders();
        healthRecordHeaders.setContentType(MediaType.APPLICATION_JSON);
        healthRecordHeaders.set("Authorization", "Bearer " + token);
        ResponseEntity<List<PatientHealthRecord>> healthRecordEntity = restTemplate.exchange(connectionURL, HttpMethod.POST, new HttpEntity<>(healthRecordHeaders), new ParameterizedTypeReference<List<PatientHealthRecord>>() {});
        List<PatientHealthRecord> patientHealthRecords = healthRecordEntity.getBody();
        for (PatientHealthRecord patientHealthRecord: patientHealthRecords) {
            patientHealthRecord.setAbhaId(aesUtils.encrypt(aesUtils.decrypt(patientHealthRecord.getAbhaId(), hospitalSecretKey), cmsSecretString));
            patientHealthRecord.setDescription(aesUtils.encrypt(aesUtils.decrypt(patientHealthRecord.getDescription(), hospitalSecretKey), cmsSecretString));
            patientHealthRecord.setHospitalName(aesUtils.encrypt(aesUtils.decrypt(patientHealthRecord.getHospitalName(), hospitalSecretKey), cmsSecretString));
            patientHealthRecord.setRecordType(aesUtils.encrypt(aesUtils.decrypt(patientHealthRecord.getRecordType(), hospitalSecretKey), cmsSecretString));
        }
        return new ResponseEntity<Response>(new Response(patientHealthRecords, 200), HttpStatus.OK);
    }

}