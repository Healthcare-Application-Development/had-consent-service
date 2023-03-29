package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.Consent;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.PatientInterface;
import jakarta.websocket.server.PathParam;
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


    @GetMapping("/getAllConsents")
    public ResponseEntity<Response> getConsentsByID(@PathParam("id") Integer id) {
        return patientInterface.getAllConsentsByID(id);
    }

    @PutMapping("/updateConsentStatus/{id}")
    public ResponseEntity<Response> updateConsentStatus(@PathVariable("id") Integer id, @RequestBody ConsentRequest consentRequest) {
        patientInterface.updateConsentStatus(id, consentRequest);
        return patientInterface.getAllConsentsByID(consentRequest.getPatientId());
    }
}