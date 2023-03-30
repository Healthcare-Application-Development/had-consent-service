package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.DoctorInterface;
import com.example.hadconsentservice.repository.ConsentItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService implements DoctorInterface {
    final ConsentItemRepository consentRepository;

    public DoctorService(ConsentItemRepository consentRepository) {
        this.consentRepository = consentRepository;
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
    public ResponseEntity<Response> getConsentsByDoctorID(Integer doctorID) {
        List<ConsentItem> consentList = consentRepository.findAllByDoctorID(doctorID);
        return new ResponseEntity<>(new Response(consentList, 200), HttpStatus.OK);
    }

}
