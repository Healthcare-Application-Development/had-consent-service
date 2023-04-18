package com.example.hadconsentservice.interfaces;

import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import org.springframework.http.ResponseEntity;

public interface PatientInterface {
    ResponseEntity<Response> getAllConsentsByID(Integer id);
    ResponseEntity<Response> updateConsentStatus(ConsentRequest consentRequest, String patientID);





}
