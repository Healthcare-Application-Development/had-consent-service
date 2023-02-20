package com.example.hadconsentservice.Doctor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoctorController {
    @GetMapping("/doctor")
    public String doctor(){
        return "doctor-controller";
    }

    @PostMapping("/doctor-request")
    public ResponseEntity<String> handleDoctorRequest(@RequestBody String requestBody) {
        return ResponseEntity.ok("Doctor's Request received successfully!");
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
