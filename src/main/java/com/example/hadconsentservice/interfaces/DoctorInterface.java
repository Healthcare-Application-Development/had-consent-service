package com.example.hadconsentservice.interfaces;

import com.example.hadconsentservice.bean.Consent;

import java.util.List;

public interface DoctorInterface {
    void sendConsentRequest(Consent consentRequest);
    List<Consent> getConsentsByDoctorID(Integer doctorID);
}
