package com.example.hadconsentservice.controller;

import com.example.hadconsentservice.bean.Consent;
import com.example.hadconsentservice.bean.ConsentRequest;
import com.example.hadconsentservice.bean.Response;
import com.example.hadconsentservice.interfaces.DoctorInterface;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/doctor")
public class DoctorController {

    final DoctorInterface doctorInterface;

    public DoctorController(DoctorInterface doctorInterface) {
        this.doctorInterface = doctorInterface;
    }

    @GetMapping("/doctor")
    public String doctor(){
        return "doctor-controller";
    }

    @PostMapping("/doctor-request")
    public ResponseEntity<Response> handleDoctorRequest(@RequestBody ConsentRequest consentRequest) throws ParseException {
        Consent consent = new Consent();
        consent.setDoctorID(consentRequest.getDoctorId());
        consent.setPatientID(consentRequest.getPatientId());
        consent.setConsentMessage(consentRequest.getRequestBody());
        consent.setConsentAcknowledged(false);
        consent.setApproved(false);
        consent.setHospitalId(consentRequest.getHospitalId());
        consent.setFromDate(new SimpleDateFormat("yyyy-MM-dd").parse(consentRequest.getFromDate()));
        consent.setToDate(new SimpleDateFormat("yyyy-MM-dd").parse(consentRequest.getToDate()));
        doctorInterface.sendConsentRequest(consent);

        return doctorInterface.getConsentsByDoctorID(consentRequest.getDoctorId());
    }

    @GetMapping("/getAllConsents")
    public ResponseEntity<Response> getConsentsByDoctorID(@PathParam("id") Integer id) {
        return doctorInterface.getConsentsByDoctorID(id);
    }
//
//    {
//        "DoctorId" : 1,
//            "PatientABHAID" : 123,
//            "RequestedConsent" : {
//        "Hospital" : {
//            "id":1,
//                    "categories": ["blood","diabetes"],
//            "dateRange": {
//                "from" : "dd-mm-yyyy",
//                        "to" : "dd-mm-yyyy"
//            }
//        }
//    }
//    }
}
