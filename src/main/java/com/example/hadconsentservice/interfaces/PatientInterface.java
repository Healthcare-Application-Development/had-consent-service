package com.example.hadconsentservice.interfaces;

import com.example.hadconsentservice.bean.Consent;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientInterface {
    ResponseEntity<Response> getAllConsentsByID(Integer id);
    ResponseEntity<Response> updateConsentStatus(Integer consentID, ConsentRequest consentRequest);

}
