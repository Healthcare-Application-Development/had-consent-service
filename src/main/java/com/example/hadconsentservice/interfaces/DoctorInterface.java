package com.example.hadconsentservice.interfaces;

import com.example.hadconsentservice.bean.Consent;
import com.example.hadconsentservice.bean.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DoctorInterface {
    ResponseEntity<Response> sendConsentRequest(Consent consentRequest);
    ResponseEntity<Response> getConsentsByDoctorID(Integer doctorID);
}
