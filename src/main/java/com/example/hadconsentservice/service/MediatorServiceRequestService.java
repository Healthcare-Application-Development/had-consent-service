package com.example.hadconsentservice.service;

import com.example.hadconsentservice.interfaces.MediatorServiceRequestInterface;
import com.example.hadconsentservice.repository.MediatorServiceRequestRepository;
import org.springframework.stereotype.Service;


@Service
public class MediatorServiceRequestService implements MediatorServiceRequestInterface {

    final MediatorServiceRequestRepository mediatorServiceRequestRepository;

    public MediatorServiceRequestService(MediatorServiceRequestRepository mediatorServiceRequestRepository) {
        this.mediatorServiceRequestRepository = mediatorServiceRequestRepository;
    }


}
