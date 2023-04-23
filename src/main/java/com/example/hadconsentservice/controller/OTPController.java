package com.example.hadconsentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hadconsentservice.bean.OtpVerification;
import com.example.hadconsentservice.otp.OTPService;


@RestController
@RequestMapping("/otp")
public class OTPController {
    @Autowired
    OTPService otpService;
    
    @PostMapping("/send")
    public String send(@RequestBody OtpVerification otpVerification) {
        return otpService.sendKey(otpVerification);
    }

    @PostMapping("/verify")
    public String verify(@RequestBody OtpVerification otpVerification) {
        return otpService.verifyKey(otpVerification);
    }
}
