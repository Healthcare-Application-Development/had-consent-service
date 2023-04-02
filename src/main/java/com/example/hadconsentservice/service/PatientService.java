package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.PatientInterface;
import com.example.hadconsentservice.repository.ConsentItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService implements PatientInterface {
    final ConsentItemRepository consentRepository;

    public PatientService(ConsentItemRepository consentRepository) {
        this.consentRepository = consentRepository;
    }

    @Override
    public ResponseEntity<Response> getAllConsentsByID(Integer patientID) {
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
    public ResponseEntity<Response> updateConsentStatus(ConsentRequest consentRequest) {
        List<ConsentItem> consentItem = consentRepository.findAllById(consentRequest.getItemId());

        for (ConsentItem consent:consentItem) {
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

        return new ResponseEntity<>(new Response("internal server error patientService", 400), HttpStatus.NOT_FOUND);

    }
}
