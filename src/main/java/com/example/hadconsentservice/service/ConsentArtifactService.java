package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsentArtifactService {

    @Autowired
    private ConsentArtifactRepository consentArtifactRepository;

    public List<ConsentArtifact> findAllByPatientID(Integer patientID) {
        return consentArtifactRepository.findAllByPatientID(patientID);
    }

}

