package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.PatientInterface;
import com.example.hadconsentservice.service.ConsentArtifactService;
import com.example.hadconsentservice.service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/patient")
public class PatientController {
    final PatientInterface patientInterface;

    public PatientController(PatientInterface patientInterface) {
        this.patientInterface = patientInterface;
    }

    @Autowired
    private ConsentArtifactService consentArtifactService;
    @Autowired
    private ConsentService consentService;

    @GetMapping("/getAllConsents/{id}")
    public ResponseEntity<Response> findAllByPatientID(@PathVariable String id) {
        System.out.println("id : "+Integer.valueOf(id));
        List<ConsentArtifact> ca = consentArtifactService.findAllByPatientID(Integer.valueOf(id));
        return new ResponseEntity<>(new Response(ca, 200), HttpStatus.OK);
    }

    @PutMapping("/updateConsentStatus")
    public ResponseEntity<Response> updateConsentStatus( @RequestBody ConsentRequest consentRequest) {
        patientInterface.updateConsentStatus(consentRequest);
        return patientInterface.getAllConsentsByID(consentRequest.getPatientId());
    }
    @PostMapping("/consent-artifacts/revoke")
    public ConsentArtifact revokeConsentArtifact(@RequestParam Integer artifactId) {
        return consentArtifactService.revokeConsentArtifact(artifactId);
    }

    @PostMapping("/consent-item/revoke")
    public ConsentItem revokeConsentArtifactitem(@RequestParam Integer Id) {
        return consentService.revokeConsentArtifactitem(Id);
    }

}