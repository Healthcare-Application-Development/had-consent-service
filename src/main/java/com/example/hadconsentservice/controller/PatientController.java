package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.PatientInterface;
import com.example.hadconsentservice.security.TokenManager;
import com.example.hadconsentservice.service.ConsentArtifactService;
import com.example.hadconsentservice.service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAllConsents/{id}")
    public ResponseEntity<Response> findAllByPatientID(@RequestHeader("Authorization") String authorization, @PathVariable String id) {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        if (!username.equals(String.valueOf(id))) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        List<ConsentArtifact> ca = consentArtifactService.findAllByPatientID(id);
        return new ResponseEntity<>(new Response(ca, 200), HttpStatus.OK);
    }

    @PutMapping("/updateConsentItemStatus")
    public ResponseEntity<Response> updateConsentItemStatus(@RequestHeader("Authorization") String authorization, @RequestBody ConsentRequest consentRequest) {
        String patientID = consentRequest.getPatientId();
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        if (!username.equals(patientID)) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        ResponseEntity<Response> res = patientInterface.updateConsentStatus(consentRequest, String.valueOf(patientID));
        if (res.getStatusCode() == HttpStatus.FORBIDDEN) {
            return res;
        }
        if (res.getStatusCode() == HttpStatus.NOT_FOUND) {
            return res;
        }
        return patientInterface.getAllConsentsByID(consentRequest.getPatientId());
    }

    @PutMapping("/updateConsentStatus")
    public ResponseEntity<Response> updateConsentStatus(@RequestHeader("Authorization") String authorization, @RequestBody ConsentRequest consentRequest) {
        String patientID = consentRequest.getPatientId();
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        if (!username.equals(String.valueOf(patientID))) {
            return new ResponseEntity<>(new Response("Authorization Failed", 403), HttpStatus.FORBIDDEN);
        }
        ResponseEntity<Response> res = consentArtifactService.updateConsentStatus(consentRequest, String.valueOf(patientID));
        if (res.getStatusCode() == HttpStatus.FORBIDDEN) {
            return res;
        }
        return patientInterface.getAllConsentsByID(consentRequest.getPatientId());
    }
    @PostMapping("/consent-artifacts/revoke")
    public ConsentArtifact revokeConsentArtifact(@RequestParam Integer artifactId) {
        return consentArtifactService.revokeConsentArtifact(artifactId);
    }

    @PostMapping("/consent-item/revoke")
    public ConsentItem revokeConsentArtifactitem(@RequestParam Integer Id) {
        return consentService.revokeConsentArtifactItem(Id);
    }

}