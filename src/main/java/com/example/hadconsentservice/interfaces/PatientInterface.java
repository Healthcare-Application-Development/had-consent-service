package com.example.hadconsentservice.interfaces;

import com.example.hadconsentservice.bean.Consent;
import com.example.hadconsentservice.bean.ConsentRequest;

import java.util.List;

public interface PatientInterface {
    List<Consent> getAllConsentsByID(Integer id);
    Consent updateConsentStatus(Integer consentID, ConsentRequest consentRequest);

}
