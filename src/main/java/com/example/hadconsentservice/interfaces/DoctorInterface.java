package com.example.hadconsentservice.interfaces;

import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.DelegateConsent;
import com.example.hadconsentservice.bean.Response;
import org.springframework.http.ResponseEntity;

public interface DoctorInterface {
    ResponseEntity<Response> sendConsentRequest(ConsentItem consentRequest);
    ResponseEntity<Response> getConsentsByDoctorID(String doctorID);
    ResponseEntity<Response> delegateConsent(DelegateConsent delegateConsent);
    ResponseEntity<Response> getDelegateConsentsByFromDocID(String fromDocID);
    ResponseEntity<Response> getDelegateConsentsByToDocID(String toDocID);
    ResponseEntity<Response> updateDelegatedConsentStatus(Integer id, String docID);
    ResponseEntity<Response> updateConsentStatus(ConsentRequest consentRequest);
}
