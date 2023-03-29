package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.Consent;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.PatientInterface;
import com.example.hadconsentservice.repository.ConsentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService implements PatientInterface {
    final ConsentRepository consentRepository;

    public PatientService(ConsentRepository consentRepository) {
        this.consentRepository = consentRepository;
    }

    @Override
    public ResponseEntity<Response> getAllConsentsByID(Integer patientID) {
        List<Consent> consentList = consentRepository.findAllByPatientID(patientID);
        List<Consent> updatedConsentList = new ArrayList<>();

        for (Consent consent: consentList) {
            if (!consent.getConsentAcknowledged() || (consent.getConsentAcknowledged() && consent.getApproved())) {
                updatedConsentList.add(consent);
            }
        }
        return new ResponseEntity<>(new Response(updatedConsentList, 200), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateConsentStatus(Integer consentID, ConsentRequest consentRequest) {
        Optional<Consent> optionalConsent = consentRepository.findById(consentID);
        Consent consent = null;
        if (optionalConsent.isPresent())
            consent = optionalConsent.get();
        else
            return null;
        if (consent.getConsentAcknowledged()) {
            return null;
        }
        consent.setConsentAcknowledged(true);
        consent.setApproved(consentRequest.getApproved());
        try {
            consentRepository.save(consent);
            return new ResponseEntity<>(new Response(consent, 200), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(e.getMessage(), 400), HttpStatus.NOT_FOUND);
        }

    }
}
