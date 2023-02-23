package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.Consent;
import com.example.hadconsentservice.interfaces.DoctorInterface;
import com.example.hadconsentservice.repository.ConsentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService implements DoctorInterface {
    final ConsentRepository consentRepository;

    public DoctorService(ConsentRepository consentRepository) {
        this.consentRepository = consentRepository;
    }

    @Override
    public void sendConsentRequest(Consent consentRequest) {
        consentRepository.save(consentRequest);
    }

    @Override
    public List<Consent> getConsentsByDoctorID(Integer doctorID) {
        List<Consent> consentList = consentRepository.findAllByDoctorID(doctorID);
        return consentList;
    }

}
