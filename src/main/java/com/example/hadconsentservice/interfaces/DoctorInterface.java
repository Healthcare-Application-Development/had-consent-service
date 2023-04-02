package com.example.hadconsentservice.interfaces;

import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.Response;
import org.springframework.http.ResponseEntity;

public interface DoctorInterface {
    ResponseEntity<Response> sendConsentRequest(ConsentItem consentRequest);
    ResponseEntity<Response> getConsentsByDoctorID(Integer doctorID);
}
