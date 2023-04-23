package com.example.hadconsentservice.service;

import com.example.hadconsentservice.bean.*;
import com.example.hadconsentservice.interfaces.GuardianInterface;
import com.example.hadconsentservice.interfaces.PatientInterface;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;

import com.example.hadconsentservice.repository.GuardianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GuardianService implements GuardianInterface {
    @Autowired
    public final GuardianRepository guardianRepository;

    public GuardianService(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    public Optional<Guardian> findByID(Long guardianID) {
        return guardianRepository.findById(guardianID);
    }

}
