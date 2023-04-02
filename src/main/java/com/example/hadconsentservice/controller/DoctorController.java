package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.DoctorInterface;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;
import com.example.hadconsentservice.service.ConsentService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private ConsentArtifactRepository consentArtifactRepository;

    @Autowired
    private ConsentItemRepository consentItemRepository;

    @Autowired
    private ConsentService consentService;

    final DoctorInterface doctorInterface;

    public DoctorController(DoctorInterface doctorInterface) {
        this.doctorInterface = doctorInterface;
    }

    @GetMapping("/doctor")
    public String doctor(){
        return "doctor-controller";
    }

    @PostMapping("/store-consent-request")
    public ResponseEntity<String> storeConsentArtifact(@RequestBody ConsentArtifact consentArtifact) {
        boolean res = consentService.saveConsentArtifactWithItems(consentArtifact);
        if (res != false) {
            return new ResponseEntity<>("Consent artifact saved successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to save consent artifact", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/doctor-request")
    public ResponseEntity<Response> handleDoctorRequest(@RequestBody List<ConsentRequest> consentRequests) throws ParseException {
        List<Response> responses = new ArrayList<>();
        List<ConsentItem> consents = consentRequests.stream()
                .map(req -> {
                    ConsentItem consent = new ConsentItem();
                    consent.setDoctorID(req.getDoctorId());
                    consent.setPatientID(req.getPatientId());
                    consent.setConsentMessage(req.getRequestBody());
                    consent.setConsentAcknowledged(false);
                    consent.setApproved(false);
                    consent.setHospitalId(req.getHospitalId());
                    try {
                        consent.setFromDate(new SimpleDateFormat("yyyy-MM-dd").parse(req.getFromDate()));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        consent.setToDate(new SimpleDateFormat("yyyy-MM-dd").parse(req.getToDate()));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    return consent;
                })
                .collect(Collectors.toList());

        for (ConsentItem consent  : consents) {
            Response response = doctorInterface.sendConsentRequest(consent).getBody();
            responses.add(response);
        }
        consents.forEach(doctorInterface::sendConsentRequest);

        return new ResponseEntity<>(new Response(responses, 200), HttpStatus.OK);
    }

    @GetMapping("/getAllConsents")
    public ResponseEntity<Response> getConsentsByDoctorID(@PathParam("id") Integer id) {
        return doctorInterface.getConsentsByDoctorID(id);
    }
}
//
//    {
//        "artifactId": 1,
//        "doctorID": {
//                "id": 1,
//                "uprnID": "John",
//                "name": "Doe",
//        },
//        "patientID": {
//                "patientID": 2,
//                "patientName": "Jane",
//                "dob": "1990-01-01",
//                "phone_number": "6361192457"
//        },
//        "timestamp": "2022-03-30T15:00:00Z",
//        "emergency": false,
//        "consentItems": [
//              {
//                "id": 1,
//                "doctorID": 1,
//                "patientID": 2,
//                "consentMessage": "xray",
//                "consentAcknowledged": false,
//                "approved": false,
//                "fromDate": "2022-04-01",
//                "toDate": "2022-04-30",
//                "hospitalId": 1
//        },
//        {
//            "id": 2,
//                "doctorID": 1,
//                "patientID": 2,
//                "consentMessage": "blood-report",
//                "consentAcknowledged": false,
//                "approved": false,
//                "fromDate": "2022-04-01",
//                "toDate": "2022-04-30",
//                "hospitalId": 2
//        }
//    ]
//    }