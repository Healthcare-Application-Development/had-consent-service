package com.example.hadconsentservice.otp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.hadconsentservice.bean.OtpVerification;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

@Service
public class OTPService {
	@Value("${TWILIO_ACCOUNT_SID}")
	String accountSid;

	@Value("${TWILIO_AUTH_KEY}")
	String authKey;

    @Value("${TWILIO_VERIFY_SID}")
    String verifySid;
    
    public String sendKey(OtpVerification otpVerification) {
        Twilio.init(accountSid, authKey);
        Verification verification = Verification.creator(verifySid, otpVerification.getMobileNumber(), "sms").create();
        return verification.getStatus();
    }

    public String verifyKey(OtpVerification otpVerification) {
        Twilio.init(accountSid, authKey);
        VerificationCheck verificationCheck = VerificationCheck.creator(verifySid)
            .setTo(otpVerification.getMobileNumber())
            .setCode(otpVerification.getOtp())            
            .create();

        return verificationCheck.getStatus();
    }
}
